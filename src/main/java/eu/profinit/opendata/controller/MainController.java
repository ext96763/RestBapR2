package eu.profinit.opendata.controller;

import eu.profinit.opendata.mapper.RecordMapper;
import eu.profinit.opendata.model.Record;
import eu.profinit.opendata.utils.DateParser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Produces;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * Created by livsu on 15.04.2017.
 */


@RestController
public class MainController {

    @Autowired
    RecordMapper mapper;

    @Autowired
    DateParser dateParser;

    @ApiOperation(value = "Search by name", notes = "Any part of given name of tender will by searched", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Name of tender", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<List<Record>> getByName(@RequestParam(value = "name", required = false) String name) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchByName(name));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }



    @ApiOperation(value = "Search in Suppliers", notes = "Search in suppliers by name or ICO", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Name of supplier", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ico", value = "ICO of supplier", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/suppliers/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<List<Record>> getSupplier(@RequestParam(value = "ico", required = false) String ico,
                                                                  @RequestParam(value = "name", required = false) String name) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchSupplier(ico, name));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }



    @ApiOperation(value = "Search in Buyers", notes = "Search in buyers by name or ICO", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Name of supplier", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ico", value = "ICO of buyer", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/buyers/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<List<Record>> getCustomer(@RequestParam(value = "ico", required = false) String ico,
                                                                  @RequestParam(value = "name", required = false) String name,
                                                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchCustomer(ico, name));

        Link link = linkTo(methodOn(MainController.class).getCustomer(ico, name, page+1)).withRel("next");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link",link.toString());
        headers.add("X-Total-Record-Count",records.size()+"");
        headers.add("X-Total-Page-Count",records.size()/20+"");


        return new ResponseEntity<List<Record>>(records.subList((page - 1)*20, (page - 1)*20+20), headers, HttpStatus.OK);
    }



    @ApiOperation(value = "Search Tenders", notes = "Search tender by part of a given name, volume or date between created date and end date ", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Part of name of the tender", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "volume", value = "Precise volume of the particular tender", required = false, dataType = "Double", paramType = "query"),
            @ApiImplicitParam(name = "dateCreated", value = "Date of tender creation", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "dueDate", value = "Date of tender end", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/tenders/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<List<Record>> getTender(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "dateFrom", required = false)  String mappedDateFrom,
                                                  @RequestParam(value = "dateTo", required = false)  String mappedDateTo,
                                                  @RequestParam(value = "volume", required = false) Double volume) throws ParseException {

        Date dateFrom= dateParser.parseDate(mappedDateFrom);
        Date dateTo= dateParser.parseDate(mappedDateTo);

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchTender(name, dateFrom , dateTo, volume));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }

}
