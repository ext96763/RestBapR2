package eu.profinit.opendata.controller;

import eu.profinit.opendata.mapper.RecordMapper;
import eu.profinit.opendata.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by livsu on 15.04.2017.
 */


@RestController
public class MainController {

    @Autowired
    RecordMapper mapper;

    public ResponseEntity<List<Record>> getSupplier(@RequestParam(value = "ico", required = false) String ico, @RequestParam(value = "name", required = false) String name) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchSupplier(ico, name));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }

    @RequestMapping(value = "/customer/search", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Record>> getCustomer(@RequestParam(value = "ico", required = false) String ico, @RequestParam(value = "name", required = false) String name) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchCustomer(ico, name));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }




}
