package eu.profinit.opendata.controller;


import eu.profinit.opendata.ipfilter.IpTimeWindowManager;
import eu.profinit.opendata.mapper.RecordMapper;
import eu.profinit.opendata.model.*;
import eu.profinit.opendata.utils.DateParser;
import eu.profinit.opendata.utils.LinkSolver;
import eu.profinit.opendata.utils.PageCalc;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
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

//TODO finish logging at other classes, fix time value on logfile/xml. Finish api endpoints. Change port, path to logging

@RestController
public class MainController implements ErrorController{

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

    private static Logger logger = LogManager.getLogger(MainController.class);

    //Limit of records for one apiCall
    private Long sizeLimit = 100L;

    @Override
    public String getErrorPath() {
        return null;
    }

    @ApiOperation(value = "Search part of name of any record", notes = "Any part of given name of tender will by searched ", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Part of record name", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of records", required = false, dataType = "string", paramType = "query")

    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<PartialRecord>> getRecordsByNameShortDetail(@RequestParam(value = "name", required = true) String name,
                                                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) throws ParseException {

        List<PartialRecord> partialRecord = new ArrayList<>();
        partialRecord.addAll(mapper.searchByNamePartialRecord(name));
        List errorParameterList = new ArrayList();
        List<PartialRecord> cutRecord = new ArrayList<>();
        cutRecord = PageCalc.pageCalc(partialRecord, page, size);

        logger.info("REQ started on path - /search, parameters:  size[" + size + "], " + "page[" + page + "], " + "keyWord[" + name + "]");
        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > sizeLimit || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecord.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<PartialRecord>>(cutRecord, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecord.size() <= size) {
            sb.append(linkSolver.firstLinkOfRecordById(partialRecord, page, size, name) + ",");
            sb.append(linkSolver.nextLinkOfRecordById(partialRecord, page, size, name) + ",");
            sb.append(linkSolver.prevLinkOfRecordById(partialRecord, page, size, name) + ",");
            sb.append(linkSolver.lastLinkOfRecordById(partialRecord, page, size, name));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, partialRecord) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(partialRecord, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<PartialRecord>>(cutRecord, headers, HttpStatus.OK);
        } else if (page > cutRecord.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<PartialRecord>>(cutRecord, headers, HttpStatus.OK);
    }


    @ApiOperation(value = "Search by id one particular record", notes = "Search one particular record by unique ID ", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Id of record", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Number of page", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of records", required = false, dataType = "string", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/search/record", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<Record>> getRecordById(@RequestParam(value = "id", required = false) Long id,
                                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                               @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {

        logger.info("REQ started on path - /search/record, parameters:  size[" + size + "], " + "page[" + page + "], " + "iD[" + id + "]");
        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchByIdFullRecord(id));
        logger.info("DB select call: searchByIdFullRecord  iD[" + id + "]");
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);
        List errorParameterList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();


        if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkName(records, page, size, id) + ",");
            sb.append(linkSolver.nextLinkName(records, page, size, id) + ",");
            sb.append(linkSolver.prevLinkName(records, page, size, id) + ",");
            sb.append(linkSolver.lastLinkName(records, page, size, id));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());
            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
    }


    @ApiOperation(value = "Search in Suppliers by part of given name of the company or ICO", notes = "Search in suppliers by name or ICO", produces = "application/json")
    @ApiResponses(value = {
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
    public @ResponseBody
    ResponseEntity<List<PartialRecord>> getSuppliersShortDetail(@RequestParam(value = "ico", required = false) String ico,
                                                                @RequestParam(value = "name", required = false) String name,
                                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {

        logger.info("REQ started on path - /suppliers/search, parameters:  size[" + size + "], " + "page[" + page + "], " + "keyWord[" + name + "]");
        List<PartialRecord> records = new ArrayList<>();
        records.addAll(mapper.searchSuppliersByNameOrIcoShortDetail(ico, name));
        List errorParameterList = new ArrayList();
        List<PartialRecord> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > sizeLimit || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkSupplier(records, page, size, name, ico) + ",");
            sb.append(linkSolver.nextLinkSupplier(records, page, size, name, ico) + ",");
            sb.append(linkSolver.prevLinkSupplier(records, page, size, name, ico) + ",");
            sb.append(linkSolver.lastLinkSupplier(records, page, size, name, ico));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Search in Suppliers by ID", notes = "Search in suppliers by ID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Id of supplier", required = true, dataType = "long", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/supplier/record", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<Record>> getSupplierById(@RequestParam(value = "id", required = true) Long id,
                                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                        @RequestParam(value = "size", required = false, defaultValue = "1") Integer size) {

        logger.info("REQ started on path - /suppliers/record, parameters:  id[" + id + "]");
        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchSupplierByIdFullDetail(id));
        List errorParameterList = new ArrayList();
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records,page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkSupplierById(records, page, size, id) + ",");
            sb.append(linkSolver.nextLinkSupplierById(records, page, size, id) + ",");
            sb.append(linkSolver.prevLinkSupplierById(records, page, size, id) + ",");
            sb.append(linkSolver.lastLinkSupplierById(records, page, size, id));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
    }


    @ApiOperation(value = "Search in Buyers by part of name of customer or ICO", notes = "Search in buyers by name or ICO", produces = "application/json")
    @ApiResponses(value = {
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
    public @ResponseBody
    ResponseEntity<List<PartialRecord>> getBuyersShortDetail(@RequestParam(value = "ico", required = false) String ico,
                                                             @RequestParam(value = "name", required = false) String name,
                                                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {

        logger.info("REQ started on path - /buyers/search, parameters:  size[" + size + "], " + "page[" + page + "], " + "keyWord[" + name + "]");
        List<PartialRecord> records = new ArrayList<>();
        records.addAll(mapper.searchBuyersByNameOrIcoShortDetail(ico, name));
        List errorParameterList = new ArrayList();
        List<PartialRecord> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > sizeLimit || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkBuyer(records, page, size, name, ico) + ",");
            sb.append(linkSolver.nextLinkBuyer(records, page, size, name, ico) + ",");
            sb.append(linkSolver.prevLinkBuyer(records, page, size, name, ico) + ",");
            sb.append(linkSolver.lastLinkBuyer(records, page, size, name, ico));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Search in Buyers by ID", notes = "Search in buyers by ID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Record ID of buyer", required = true, dataType = "long", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/buyer/record", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<Record>> getBuyerById(@RequestParam(value = "id", required = true) Long id,
                                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", required = false, defaultValue = "1") Integer size) {

        logger.info("REQ started on path - /buyer/record, parameters:  id[" + id + "]");
        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchBuyerByIdFullDetail(id));
        List errorParameterList = new ArrayList();
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records,page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkBuyerById(records, page, size, id) + ",");
            sb.append(linkSolver.nextLinkBuyerById(records, page, size, id) + ",");
            sb.append(linkSolver.prevLinkBuyerById(records, page, size, id) + ",");
            sb.append(linkSolver.lastLinkBuyerById(records, page, size, id));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
    }


    @ApiOperation(value = "Search in Tenders", notes = "Search tender by part of a given name, volume or date between created date and end date ", produces = "application/json")
    @ApiResponses(value = {
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
    public @ResponseBody
    ResponseEntity<List<PartialRecord>> getTendersShortDetail(@RequestParam(value = "name", required = false) String name,
                                                              @RequestParam(value = "dateFrom", required = false) String mappedDateFrom,
                                                              @RequestParam(value = "dateTo", required = false) String mappedDateTo,
                                                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                              @RequestParam(value = "volumeFrom", required = false) Double volumeFrom,
                                                              @RequestParam(value = "volumeTo", required = false) Double volumeTo) throws ParseException {

        logger.info("REQ started on path - /tenders/search, parameters:  size[" + size + "], " + "page[" + page + "], " + "keyWord[" + name + "], " + "dateFrom[" + mappedDateFrom + "], " + "dateTo[" + mappedDateTo + "], " + "volumeFrom[" + volumeFrom + "], " + "volumeTo[" + volumeTo + "]");
        Date dateFrom = dateParser.parseDate(mappedDateFrom);
        Date dateTo = dateParser.parseDate(mappedDateTo);

        List<PartialRecord> records = new ArrayList<>();
        records.addAll(mapper.searchTendersByNameOrDateOrVolumeShortDetail(name, dateFrom, dateTo, volumeFrom, volumeTo));
        List errorParameterList = new ArrayList();
        List<PartialRecord> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records, page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (size > sizeLimit || size <= 0) {
            errorParameterList.add("Bad request parameter: " + size + ". Min value for size is [1] and max value for size is [100]");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkTender(records, page, size, volumeFrom, volumeTo, mappedDateFrom, mappedDateFrom, name) + ",");
            sb.append(linkSolver.nextLinkTender(records, page, size, volumeFrom, volumeTo, mappedDateFrom, mappedDateFrom, name) + ",");
            sb.append(linkSolver.prevLinkTender(records, page, size, volumeFrom, volumeTo, mappedDateFrom, mappedDateFrom, name) + ",");
            sb.append(linkSolver.lastLinkTender(records, page, size, volumeFrom, volumeTo, mappedDateFrom, mappedDateFrom, name));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<PartialRecord>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<PartialRecord>>(cutRecords, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Search in Tenders by ID", notes = "Search in tenders by ID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Record.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Record ID of buyer", required = true, dataType = "long", paramType = "query")
    })
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/tender/record", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<Record>> getTenderById(@RequestParam(value = "id", required = true) Long id,
                                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                              @RequestParam(value = "size", required = false, defaultValue = "1") Integer size) {

        logger.info("REQ started on path - /tender/record, parameters:  id[" + id + "]");
        List<Record> records = new ArrayList<>();
        records.addAll(mapper.searchTenderByIdFullRecord(id));
        List errorParameterList = new ArrayList();
        List<Record> cutRecords = new ArrayList<>();
        cutRecords = PageCalc.pageCalc(records,page, size);

        HttpHeaders headers = new HttpHeaders();
        StringBuilder sb = new StringBuilder();

        if (page <= 0) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            logger.error(errorParameterList);
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        } else if (cutRecords.isEmpty()) {
            logger.info("Records where not found in DB, headers:" + headers);
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.NOT_FOUND);
        } else if (cutRecords.size() <= size) {
            sb.append(linkSolver.firstLinkTenderById(records, page, size, id) + ",");
            sb.append(linkSolver.nextLinkTenderById(records, page, size, id) + ",");
            sb.append(linkSolver.prevLinkTenderById(records, page, size, id) + ",");
            sb.append(linkSolver.lastLinkTenderById(records, page, size, id));
            headers.add("Links", sb.toString());
            headers.add("X-Total-Records", linkSolver.totalPages(page, records) + "");
            headers.add("X-Total-Page-Count", linkSolver.pageCount(records, size) + "");
            headers.add("X-Forwarded-For", ipTimeWindowManager.getIp());

            logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
            return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
        } else if (page > cutRecords.size() / size) {
            errorParameterList.add("Bad request parameter: " + page + ". Only natural numbers are accepted. Max value of page can't exceed total number of pages");
            return new ResponseEntity<List<Record>>(errorParameterList, HttpStatus.BAD_REQUEST);
        }
        logger.info("REQ for IP:" + ipTimeWindowManager.getIp() + " succesfull.");
        return new ResponseEntity<List<Record>>(cutRecords, headers, HttpStatus.OK);
    }


    @ApiOperation(value = "Find last update date", notes = "Find last update of database", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Failure")})
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/lastUpdate", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<Retrieval>> getLastUpadte() {

        logger.info("REQ started on path - /lastUpdate");

        List<Retrieval> retrievals = new ArrayList<>();
        retrievals.addAll(mapper.findLastDate());
        return new ResponseEntity<List<Retrieval>>(retrievals, HttpStatus.OK);
    }


    @ApiOperation(value = "Find total count of records", notes = "All records count in DB", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Failure")})
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/count/totalRecords", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<TotalRecords>> getTotalRecordsCount() {

        logger.info("REQ started on path - /totalRecords");
        List<TotalRecords> totalRecords = new ArrayList<>();
        totalRecords.addAll(mapper.countAllRecords());
        return new ResponseEntity<List<TotalRecords>>(totalRecords, HttpStatus.OK);
    }

    @ApiOperation(value = "Find total count of Buyers", notes = "All records count of buyers in DB", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Failure")})
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/count/totalBuyers", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<TotalBuyers>> getTotalBuyersCount() {

        logger.info("REQ started on path - /totalBuyers");
        List<TotalBuyers> totalBuyers = new ArrayList<>();
        totalBuyers.addAll(mapper.countAllBuyers());
        return new ResponseEntity<List<TotalBuyers>>(totalBuyers, HttpStatus.OK);
    }

    @ApiOperation(value = "Find total count of Suppliers", notes = "All records count of suppliers in DB", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Failure")})
    @Produces(value = "application/json")
    @CrossOrigin()
    @RequestMapping(value = "/count/totalSuppliers", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<TotalSuppliers>> getTotalSuppliersCount() {

        logger.info("REQ started on path - /totalSuppliers");
        List<TotalSuppliers> totalSuppliers = new ArrayList<>();
        totalSuppliers.addAll(mapper.countAllSuppliers());
        return new ResponseEntity<List<TotalSuppliers>>(totalSuppliers, HttpStatus.OK);
    }
}