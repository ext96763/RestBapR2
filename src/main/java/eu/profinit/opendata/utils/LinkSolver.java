package eu.profinit.opendata.utils;

import eu.profinit.opendata.controller.MainController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Class that resolves Links that are later added to headers in a string format.
 */

@Component
public class LinkSolver {

    public String nextLinkBuyer(List records, Integer page, Integer size, String ico, String name){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
            if (page < pageCount -1) {
                Link next = linkTo(methodOn(MainController.class).getBuyersShortDetail(ico, name, page + 1, size)).withRel("next");
                nex = next.toString();
            }
            return nex;
    }

    public String prevLinkBuyer(List records, Integer page, Integer size, String ico, String name){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getBuyersShortDetail(ico, name, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPages (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCount (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkBuyer(List records, Integer page, Integer size, String ico, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

            page = pageCount - 1;
            Link last = linkTo(methodOn(MainController.class).getBuyersShortDetail(ico, name, page , size)).withRel("last");

        return last.toString();
    }

    public String firstLinkBuyer(List records, Integer page, Integer size, String ico, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getBuyersShortDetail(ico, name, page , size)).withRel("first");

        return first.toString();
    }

    public String nextLinkName(List records, Integer page, Integer size, Long id){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getRecordById(id, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkName(List records, Integer page, Integer size, Long id){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getRecordById(id, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesName (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountName (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkName(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getRecordById(id, page , size)).withRel("last");

        return last.toString();
    }

    public String firstLinkName(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getRecordById(id, page , size)).withRel("first");

        return first.toString();
    }

    public String nextLinkSupplier(List records, Integer page, Integer size, String ico, String name){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getSuppliersShortDetail(ico, name, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkSupplier(List records, Integer page, Integer size, String ico, String name){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getSuppliersShortDetail(ico, name, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesSupplier (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountSupplier (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkSupplier(List records, Integer page, Integer size, String ico, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getSuppliersShortDetail(ico, name, page , size)).withRel("last");

        return last.toString();
    }

    public String firstLinkSupplier(List records, Integer page, Integer size, String ico, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = 1;
        Link first = linkTo(methodOn(MainController.class).getSuppliersShortDetail(ico, name, page , size)).withRel("first");

        return first.toString();
    }

    public String nextLinkBuyerById(List records, Integer page, Integer size, Long id){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getBuyerById(id, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkSupplierById(List records, Integer page, Integer size, Long id){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getSupplierById(id, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesSupplierById (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountSupplierById (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkSupplierById(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getSupplierById(id, page, size)).withRel("last");

        return last.toString();
    }

    public String firstLinkSupplierById(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = 1;
        Link first = linkTo(methodOn(MainController.class).getSupplierById(id, page, size)).withRel("first");

        return first.toString();
    }

    public String nextLinkSupplierById(List records, Integer page, Integer size, Long id){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getSupplierById(id, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkBuyerById(List records, Integer page, Integer size, Long id){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getBuyerById(id, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesBuyerById (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountBuyerById (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkBuyerById(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getBuyerById(id, page, size)).withRel("last");

        return last.toString();
    }

    public String firstLinkBuyerById(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = 1;
        Link first = linkTo(methodOn(MainController.class).getBuyerById(id, page, size)).withRel("first");

        return first.toString();
    }

    public String nextLinkTender(List records, Integer page, Integer size, Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name) throws ParseException{
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getTendersShortDetail(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkTender(List records, Integer page, Integer size,  Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name)throws ParseException{
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getTendersShortDetail(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesTender (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountTender (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkTender(List records, Integer page, Integer size, Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name)throws ParseException{

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getTendersShortDetail(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("last");

        return last.toString();
    }

    public String firstLinkTender(List records, Integer page, Integer size, Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name)throws ParseException{

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getTendersShortDetail(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("first");

        return first.toString();
    }

    public String prevLinkTenderById(List records, Integer page, Integer size, Long id){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getTenderById(id, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesTenderById (Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountTenderById (List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkTenderById(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getTenderById(id, page, size)).withRel("last");

        return last.toString();
    }

    public String firstLinkTenderById(List records, Integer page, Integer size, Long id){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = 1;
        Link first = linkTo(methodOn(MainController.class).getTenderById(id, page, size)).withRel("first");

        return first.toString();
    }

    public String nextLinkTenderById(List records, Integer page, Integer size, Long id){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getTenderById(id, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String nextLinkOfRecordById(List records, Integer page, Integer size, String name) throws ParseException{
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getRecordsByNameShortDetail(name, page, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkOfRecordById(List records, Integer page, Integer size, String name) throws ParseException {
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getRecordsByNameShortDetail(name, page - 1, size)).withRel("previous");
            prev = previous.toString();
        }
        return prev;
    }

    public Integer totalPagesOfRecordById (long id,Integer page, List records) {
        return records.size();
    }

    public  Integer pageCountOfRecordById (Long id, List records, Integer size){
        return records.size()/size;
    }

    public String lastLinkOfRecordById(List records, Integer page, Integer size, String name)throws ParseException{

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getRecordsByNameShortDetail(name, page, size)).withRel("last");

        return last.toString();
    }

    public String firstLinkOfRecordById(List records, Integer page, Integer size, String name)throws ParseException{

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getRecordsByNameShortDetail(name, page, size)).withRel("first");

        return first.toString();
    }
}
