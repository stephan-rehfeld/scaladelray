/*
 * Copyright 2015 Stephan Rehfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scaladelray.loader

import akka.actor.{Props, ActorSystem, ActorRef, Actor}
import scaladelray.material.OldMaterial
import scaladelray.geometry.Geometry
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._


/**
 * The actor that loags and manages the resources. The actor may starts more actors to load more resources.
 */
private class ResourceManager extends Actor {

  /**
   * The internal resource cache.
   */
  private val cache = scala.collection.mutable.Map[String, Geometry]()

  /**
   * A map that contains current loading operations.
   */
  private val runningLoads = scala.collection.mutable.Map[String, Set[ActorRef]]()

  override def receive = {
    case m : ResourceManager.LoadResource =>
      if( cache.contains( m.fileName ) ) sender ! cache( m.fileName )
      else if( runningLoads.contains( m.fileName ) ) runningLoads += m.fileName -> (runningLoads( m.fileName ) + sender)
      else m.fileName match {
        case m.fileName if m.fileName.endsWith( ".obj" ) =>
          val a = context.actorOf( Props[ResourceManager.OBJResourceLoadActor] )
          runningLoads += (m.fileName -> (Set()+sender))
          a ! m
        case _ => sender ! ResourceManager.UnknownResourceFormat()

      }

    case m : ResourceManager.DeleteCache =>
      cache.empty

    case m : ResourceManager.LoadedResource =>
      cache += (m.fileName -> m.geo)
      for( a <- runningLoads( m.fileName ) ) a ! m.geo
      runningLoads -= m.fileName

  }


}

/**
 * The resource manager loads resources like model files asynchronously and caches them for later usage.
 */
object ResourceManager {

  /**
   * A message that is sent to the resource manager actor to load a file. The actor answers by sending a LoadedResource
   * message. This message is also used internally to communicate with data type specific load actors.
   *
   * @param fileName The name of the file.
   * @param subDivideDecider The sub devide decider for the octree.
   * @param fastLoad Flag to perform faster loading, typically by not eliminating duplicated vertices.
   */
  private case class LoadResource( fileName : String, subDivideDecider : ((Int,Int) => Boolean ), fastLoad : Boolean )

  /**
   * A type specific actor for loading resources sends this message back after it loaded the resource.
   *
   * @param fileName The name of the file that has been loaded.
   * @param geo The loaded geometry.
   */
  private case class LoadedResource( fileName: String, geo : Geometry )

  /**
   * The ResourceManager actor sends back this message, if it does not support file type. It is also used by the load
   * method to report that the filetype is not supported.
   */
  case class UnknownResourceFormat()

  /**
   * The ResourceManage deletes its cache, when it receives this message.
   */
  private case class DeleteCache()

  /**
   * The reference to the resource manager actor.
   */
  private var resourceManager : Option[ActorRef] = None

  /**
   * A get function typical for singletons. If a ResourceManager actor does not exist, it is created.
   *
   * @return The ResourceManager actor.
   */
  private def getResourceManager : ActorRef = {
    if( resourceManager.isEmpty ) {
      val system = ActorSystem( "ResourceLoader" )
      resourceManager = Some( system.actorOf( Props[ResourceManager] ) )
    }
    resourceManager.get
  }

  /**
   * This function asynchronously preloads a resource. The function return immediately.
   *
   * @param fileName The name of the file.
   * @param subDivideDecider The sub devide decider for the octree.
   * @param fastLoad Flag to perform faster loading, typically by not eliminating duplicated vertices.
   */
  def preLoad( fileName : String, material : OldMaterial, subDivideDecider : ((Int,Int) => Boolean ), fastLoad : Boolean ) {
    getResourceManager ! ResourceManager.LoadResource( fileName, subDivideDecider, fastLoad )
  }

  /**
   *  This function loads a file and blocks until the resource has been loaded completely.
   *
   * @param fileName The name of the file.
   * @param subDivideDecider The sub devide decider for the octree.
   * @param fastLoad Flag to perform faster loading, typically by not eliminating duplicated vertices.
   * @return Either the loaded geometry or a [[scaladelray.loader.ResourceManager.UnknownResourceFormat]] object.
   */
  def load( fileName : String, material : OldMaterial, subDivideDecider : ((Int,Int) => Boolean ), fastLoad : Boolean ) : Either[Geometry,UnknownResourceFormat] = {
    implicit val timeout = Timeout(500 seconds)
    val future = getResourceManager ? ResourceManager.LoadResource( fileName, subDivideDecider, fastLoad )
    val result = Await.result(future, timeout.duration )
    if( result.isInstanceOf[Geometry] )
      Left(result.asInstanceOf[Geometry])
    else
      Right(result.asInstanceOf[UnknownResourceFormat])
  }

  /**
   * This function deletes the cache of the resource manager.
   */
  def deleteCache() = {
    getResourceManager ! ResourceManager.DeleteCache()
  }

  /**
   * An actor that loads OBJ files. The actor stops itself after the file has been loaded.
   */
  class OBJResourceLoadActor extends Actor {
    override def receive = {
      case m : ResourceManager.LoadResource =>
        val objLoader = new OBJLoader
        val g = objLoader.load( m.fileName, m.subDivideDecider, m.fastLoad )
        sender ! ResourceManager.LoadedResource( m.fileName, g )
        context.stop( self )
    }
  }

}
