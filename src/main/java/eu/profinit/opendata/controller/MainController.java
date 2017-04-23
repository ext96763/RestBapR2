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
            @ApiImplicitParam(name = "name", value = "Name of tender", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<List<Record>> getByName(@RequestParam(value = "name", required = false) String name) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchByName(name));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }




    @RequestMapping(value = "/suppliers/search", method = RequestMethod.GET, produces = "application/json")
    @Produces(value = "application/json")
    @CrossOrigin()
    public @ResponseBody ResponseEntity<List<Record>> getSupplier(@RequestParam(value = "ico", required = false) String ico,
                                                                  @RequestParam(value = "name", required = false) String name) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchSupplier(ico, name));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }




    @RequestMapping(value = "/buyers/search", method = RequestMethod.GET, produces = "application/json")
    @Produces(value = "application/json")
    @CrossOrigin()
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




    @RequestMapping(value = "/tenders/search", method = RequestMethod.GET, produces = "application/json")
    @Produces(value = "application/json")
    @CrossOrigin()
    public @ResponseBody ResponseEntity<List<Record>> getTender(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "dateCreated", required = false)  String mappedDateCreated,
                                                  @RequestParam(value = "dueDate", required = false)  String mappedDueDate,
                                                  @RequestParam(value = "volume", required = false) Double volume) throws ParseException {

        Date dateCreated= dateParser.parseDate(mappedDateCreated);
        Date dueDate= dateParser.parseDate(mappedDueDate);

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchTender(name, dateCreated , dueDate, volume));
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }

}
