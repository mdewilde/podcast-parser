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
package be.ceau.podcastparser.namespace.custom.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class Mlb implements Namespace {

	private static final String NAME = "http://mlb.mlb.com/rss/";

	@Override
	public String getName() {
		return NAME;
	}

}

// 3811 | ITEM     | display-date                                       | http://mlb.mlb.com/rss/                                                         
// 3811 | ITEM     | display-date-epoch                                 | http://mlb.mlb.com/rss/                                                         
