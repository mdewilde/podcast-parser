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
package be.ceau.podcastparser.filter;

/**
 * Logic to include or exclude specific namespaces or elements from the parse process.
 */
public interface ElementFilter {

	/**
	 * Determine whether the current element should be skipped
	 * 
	 * @param namespaceURI
	 *            {@link String} or {@code null}
	 * @param localName
	 *            {@link String}, not {@code null}
	 * @return {@code true} if the given element should be skipped
	 */
	public boolean skip(String namespaceURI, String localName);

}
