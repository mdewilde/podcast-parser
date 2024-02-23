/*
	
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

import java.time.ZonedDateTime;

public class Visibility {

	private ZonedDateTime from;
	private ZonedDateTime to;

	public ZonedDateTime getFrom() {
		return from;
	}

	public void setFrom(ZonedDateTime from) {
		this.from = from;
	}

	public ZonedDateTime getTo() {
		return to;
	}

	public void setTo(ZonedDateTime to) {
		this.to = to;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Visibility [");
		if (from != null)
			builder.append("from=").append(from).append(", ");
		if (to != null)
			builder.append("to=").append(to);
		builder.append("]");
		return builder.toString();
	}

}
