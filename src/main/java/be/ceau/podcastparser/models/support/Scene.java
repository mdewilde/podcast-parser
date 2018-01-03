/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		https://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.podcastparser.models.support;

public class Scene {
	/*
	 * Optional element to specify various scenes within a media object.
	 * It can have multiple child <media:scene> elements, where each
	 * <media:scene> element contains information about a particular
	 * scene. <media:scene> has the optional sub-elements <sceneTitle>,
	 * <sceneDescription>, <sceneStartTime> and <sceneEndTime>, which
	 * contains title, description, start and end time of a particular
	 * scene in the media, respectively.
	 * 
	 * <media:scenes> 
	 * 	<media:scene>
	 * 	 <sceneTitle>sceneTitle1</sceneTitle>
	 *   <sceneDescription>sceneDesc1</sceneDescription>
	 *   <sceneStartTime>00:15</sceneStartTime>
	 *   <sceneEndTime>00:45</sceneEndTime>
	 *  </media:scene>
	 *  <media:scene>
	 *   <sceneTitle>sceneTitle2</sceneTitle>
	 *   <sceneDescription>sceneDesc2</sceneDescription>
	 *   <sceneStartTime>00:57</sceneStartTime>
	 *   <sceneEndTime>01:45</sceneEndTime> 
	 *  </media:scene> 
	 * </media:scenes>
	 */

	private String title;
	private String description;
	private String startTime;
	private String endTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}	

}
