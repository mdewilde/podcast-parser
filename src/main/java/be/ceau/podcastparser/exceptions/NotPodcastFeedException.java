/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.exceptions;

/**
 * {@link RuntimeException} indicating that the parsed file is not RSS or Atom.
 */
public class NotPodcastFeedException extends PodcastParserException {

	private static final long serialVersionUID = 1514315686879L;

	public NotPodcastFeedException(String message) {
		super(message);
	}

	public NotPodcastFeedException(String message, String arg0) {
		super(message.replaceFirst("\\{\\}", arg0));
	}

	public NotPodcastFeedException(Throwable cause) {
		super(cause);
	}

}
