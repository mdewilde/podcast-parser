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

import java.time.ZonedDateTime;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Dates;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <p>
 * Provides syndication hints to aggregators and others picking up this RDF Site
 * Summary (RSS) feed regarding how often it is updated.
 * </p>
 * 
 * <p>
 * updatePeriod<br>
 * Describes the period over which the channel format is updated. Acceptable
 * values are: hourly, daily, weekly, monthly, yearly. If omitted, daily is
 * assumed.
 * </p>
 * 
 * <p>
 * updateFrequency<br>
 * Used to describe the frequency of updates in relation to the update period. A
 * positive integer indicates how many times in that period the channel is
 * updated. For example, an updatePeriod of daily, and an updateFrequency of 2
 * indicates the channel format is updated twice daily. If omitted a value of 1
 * is assumed.
 * </p>
 * 
 * <p>
 * updateBase<br>
 * Defines a base date to be used in concert with updatePeriod and
 * updateFrequency to calculate the publishing schedule. The date format takes
 * the form: yyyy-mm-ddThh:mm
 * </p>
 * 
 * @see http://web.resource.org/rss/1.0/modules/syndication/
 */
public class Syndication implements Namespace {

	private static final Logger logger = LoggerFactory.getLogger(Syndication.class);
	
	private static final String NAME = "http://purl.org/rss/1.0/modules/syndication/";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of("https://purl.org/rss/1.0/modules/syndication/");

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "updatePeriod" :
			// The period over which the channel format is updated.
			// Acceptable values are: hourly, daily, weekly, monthly, yearly
			ctx.getFeed().computeUpdateInfoIfAbsent().setPeriod(ctx.getElementText());
			break;
		case "updateFrequency" :
			// the frequency of updates in relation to the update period
			String frequency = ctx.getElementText();
			try {
				ctx.getFeed().computeUpdateInfoIfAbsent().setFrequency(Integer.parseInt(frequency));
			} catch (NumberFormatException e) {
				logger.debug("{} on {}", e.getMessage(), frequency);
			}
			break;
		case "updateBase" :
			// base date to calculate the publishing schedule
			ZonedDateTime temporal = Dates.parse(ctx.getElementText());
			ctx.getFeed().computeUpdateInfoIfAbsent().setBase(temporal);
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

}

/*
	corpus stats
	
     28814 	--> http://purl.org/rss/1.0/modules/syndication/ level=feed localName=updatePeriod attributes=[]]
     28800 	--> http://purl.org/rss/1.0/modules/syndication/ level=feed localName=updateFrequency attributes=[]]
       114 	--> http://purl.org/rss/1.0/modules/syndication/ level=feed localName=updateBase attributes=[]]
         2 	--> https://purl.org/rss/1.0/modules/syndication/ level=feed localName=updateFrequency attributes=[]]
         2 	--> https://purl.org/rss/1.0/modules/syndication/ level=feed localName=updatePeriod attributes=[]]
         1 	--> http://purl.org/rss/1.0/modules/syndication/ level=feed localName=updateperiod attributes=[]]

*/