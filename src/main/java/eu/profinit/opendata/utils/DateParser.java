package eu.profinit.opendata.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by livsu on 22.04.2017.
 */

@Component
public class DateParser {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public Date parseDate(String dateToConvert) throws ParseException {
        synchronized(sdf) {
            if(dateToConvert != null) {
                Date date = sdf.parse(dateToConvert);

                return date;
            }
        }
        return null;
    }
}
