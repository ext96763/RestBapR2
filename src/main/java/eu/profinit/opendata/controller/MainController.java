package eu.profinit.opendata.controller;


import eu.profinit.opendata.ipthrottlingfilter.IpTimeWindowManager;
import eu.profinit.opendata.mapper.RecordMapper;
import eu.profinit.opendata.model.Record;
import eu.profinit.opendata.utils.DateParser;
import eu.profinit.opendata.utils.LinkSolver;
import eu.profinit.opendata.utils.PageCalc;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Produces;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * MainController, in this class variables and parameters are mapping from the front end. Every method responds to particular search in data.
 * Controller using annotation for API documentation. Also controller responds with different messages in cases when data aren't returned correctly.
 */


@RestController
public class MainController {

    @Autowired
    RecordMapper mapper;

    @Autowired
    DateParser dateParser;

    @Autowired
    PageCalc pageCalc;

    @Autowired
    LinkSolver linkSolver;

    @Autowired
    IpTimeWindowManager ipTimeWindowManager;

    @ApiOperation(value = "Search by name", notes = "Any part of given name of tender will by searched", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Name of tender", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<List<Record>> getByName(@RequestParam(value = "name", required = false) String name,
                                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "30") Integer size) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchByName(name));
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);
        List errorParameterList = new ArrayList<>();

            HttpHeaders headers = new HttpHeaders();
            StringBuilder sb = new StringBuilder();

            if (size > 100 || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
                return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
            }else if (page <= 0) {
                errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
                return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
            }else if (cutRecords.isEmpty()) {
                return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
            }else if(cutRecords.size() <= size)  {
                sb.append(linkSolver.firstLinkName(records, page, size, name) + ",");
                sb.append(linkSolver.nextLinkName(records, page, size, name) + ",");
                sb.append(linkSolver.prevLinkName(records, page, size, name) + ",");
                sb.append(linkSolver.lastLinkName(records, page, size, name));
                headers.add("Links", sb.toString());
                headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
                headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
                headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());
                return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
            } else if (page > cutRecords.size() / size){
        errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
        return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
    }
        return new ResponseEntity<List<Record>>(cutRecords,headers, HttpStatus.OK);
    }




    @ApiOperation(value = "Search in Suppliers", notes = "Search in suppliers by name or ICO", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
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
                                                                  @RequestParam(value = "name", required = false) String name,
                                                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "size", required = false, defaultValue = "30") Integer size) {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchSupplier(ico, name));
        List errorParameterList = new ArrayList();
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > 100 || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }else if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }else if (cutRecords.isEmpty()) {
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        }else if(cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkSupplier(records, page, size, name, ico) + ",");
            sb.append(linkSolver.nextLinkSupplier(records, page, size, name, ico) + ",");
            sb.append(linkSolver.prevLinkSupplier(records, page, size, name, ico) + ",");
            sb.append(linkSolver.lastLinkSupplier(records, page, size, name, ico));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size){
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Record>>(cutRecords,headers, HttpStatus.OK);
    }



    @ApiOperation(value = "Search in Buyers", notes = "Search in buyers by name or ICO", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
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
                                                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "size", required = false, defaultValue = "30") Integer size)
    {

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchCustomer(ico, name));
        List errorParameterList = new ArrayList();
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > 100 || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }else if(page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }else if (cutRecords.isEmpty()) {
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        }else if(cutRecords.size() <= size){
            sb.append(linkSolver.firstLinkBuyer(records, page, size, name, ico) + ",");
            sb.append(linkSolver.nextLinkBuyer(records, page, size, name, ico) + ",");
            sb.append(linkSolver.prevLinkBuyer(records, page, size, name, ico) + ",");
            sb.append(linkSolver.lastLinkBuyer(records, page, size, name, ico));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Record>>(cutRecords,headers, HttpStatus.OK);
    }



    @ApiOperation(value = "Search Tenders", notes = "Search tender by part of a given name, volume or date between created date and end date ", produces = "application/json")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
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
                                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "30") Integer size,
                                                  @RequestParam(value = "volume", required = false) Double volume) throws ParseException {

        Date dateFrom= dateParser.parseDate(mappedDateFrom);
        Date dateTo= dateParser.parseDate(mappedDateTo);

        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchTender(name, dateFrom , dateTo, volume));
        List errorParameterList = new ArrayList();
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > 100 || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }else if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }else if (cutRecords.isEmpty()) {
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if(cutRecords.size() <= size){
            sb.append(linkSolver.firstLinkTender(records, page, size, volume, mappedDateFrom, mappedDateFrom, name) + ",");
            sb.append(linkSolver.nextLinkTender(records, page, size, volume, mappedDateFrom, mappedDateFrom, name) + ",");
            sb.append(linkSolver.prevLinkTender(records, page, size, volume, mappedDateFrom, mappedDateFrom, name) + ",");
            sb.append(linkSolver.lastLinkTender(records, page, size, volume, mappedDateFrom, mappedDateFrom, name));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size){
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Record>>(cutRecords,headers, HttpStatus.OK);
    }

}