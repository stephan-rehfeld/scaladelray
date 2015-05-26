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

package scaladelray.rendering.concurrency

/**
 * DistributionManager is the abstract base class for any service that manages the distribution of rendering tasks.
 * Tasks can be performed sequentially, concurrently on the local machine (e.g. SMP or CMP systems), concurrently
 * across local network (clustering), or to a cloud.
 */
abstract class DistributionManager {

}
