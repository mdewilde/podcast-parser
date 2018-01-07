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
package be.ceau.podcastparser.api;

import java.io.Reader;

import be.ceau.podcastparser.exceptions.PodcastParserException;
import be.ceau.podcastparser.models.core.Feed;

/**
 * Parser podcast XML feeds
 */
public interface PodcastParser {

	/**
	 * Parse the given XML {@link String} into a {@link Feed} object.
	 *
	 * @param xml
	 *            a {@link String} object.
	 * @return a {@link Feed} object
	 * @throws {@link PodcastParserException} if any
	 */
	public Feed parse(String xml) throws PodcastParserException;

	/**
	 * Parse the given XML {@link Reader} into a {@link Feed} object.
	 *
	 * @param reader
	 *            a {@link Reader} object.
	 * @return a {@link Feed} object
	 * @throws {@link PodcastParserException} if any
	 */
	public Feed parse(Reader reader) throws PodcastParserException;

}
