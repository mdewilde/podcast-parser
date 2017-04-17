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
package be.ceau.podcastparser.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Parses several date formats.
 */
public class DateParser {

    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");
    /**
     * Class is intended for static use only
     */
    private DateParser(){}


    /**
     * Takes a string of the form [HH:]MM:SS[.mmm] and converts it to
     * milliseconds.
     *
     * @throws java.lang.NumberFormatException if the number segments contain invalid numbers.
     */
    public static long parseTimeString(final String time) {
        String[] parts = time.split(":");
        long result = 0;
        int idx = 0;
        if (parts.length == 3) {
            // string has hours
            result += Integer.parseInt(parts[idx]) * 3600000L;
            idx++;
        }
        if (parts.length >= 2) {
            result += Integer.parseInt(parts[idx]) * 60000L;
            idx++;
            result += (Float.parseFloat(parts[idx])) * 1000L;
        }
        return result;
    }

    public static String formatRFC822Date(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yy HH:mm:ss Z", Locale.US);
        return format.format(date);
    }

    public static String formatRFC3339Local(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        return format.format(date);
    }

    public static String formatRFC3339UTC(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(DEFAULT_TIME_ZONE);
        return format.format(date);
    }

}
