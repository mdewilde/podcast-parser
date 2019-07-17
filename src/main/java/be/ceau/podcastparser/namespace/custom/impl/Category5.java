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
package be.ceau.podcastparser.namespace.custom.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class Category5 implements Namespace {

	private static final String NAME = "http://www.category5.tv/";

	@Override
	public String getName() {
		return NAME;
	}

}

// 738 | ITEM     | title                                              | http://www.category5.tv/                                                        
// 738 | ITEM     | genre                                              | http://www.category5.tv/                                                        
// 738 | ITEM     | thumbnail                                          | http://www.category5.tv/                                                        
// 738 | ITEM     | season                                             | http://www.category5.tv/                                                        
// 738 | ITEM     | description                                        | http://www.category5.tv/                                                        
// 738 | ITEM     | number                                             | http://www.category5.tv/                                                        
// 738 | ITEM     | year                                               | http://www.category5.tv/                                                        
