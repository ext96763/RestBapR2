package eu.profinit.opendata.utils;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26.04.2017.
 */

@Component
public class PageCalc {

public static List pageCalc(List records, Integer page, Integer size){

    if(page <= 0){
        return new ArrayList();
    }
    if(size <= 0){
        return new ArrayList();
    }
    if(!records.isEmpty()) {
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        if (page <= pageCount) {
            return records.subList((page - 1) * size, (page - 1) * size + size);
        }
    }
        return new ArrayList();
    }
}
