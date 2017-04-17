/*
	Copyright 2017 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.stax.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>Podlove Simple Chapters</h1>
 * 
 * <p>
 * Podlove Simple Chapters is an XML 1.0 based format meant to extend file
 * formats like Atom Syndication and RSS 2.0 that reference enclosures
 * (podcasts). As the name implies, this format defines simple chapter
 * structures in media files.
 * </p>
 * 
 * @see http://podlove.org/simple-chapters
 */
public class SimpleChapters implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://podlove.org/simple-chapters");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}
