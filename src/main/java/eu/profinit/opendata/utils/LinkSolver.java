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
                Link next = linkTo(methodOn(MainController.class).getCustomer(ico, name, page + 1, size)).withRel("next");
                nex = next.toString();
            }
            return nex;
    }

    public String prevLinkBuyer(List records, Integer page, Integer size, String ico, String name){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getCustomer(ico, name, page - 1, size)).withRel("previous");
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
            Link last = linkTo(methodOn(MainController.class).getCustomer(ico, name, page , size)).withRel("last");

        return last.toString();
    }

    public String firstLinkBuyer(List records, Integer page, Integer size, String ico, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getCustomer(ico, name, page , size)).withRel("first");

        return first.toString();
    }

    public String nextLinkName(List records, Integer page, Integer size, String name){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getByName(name, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkName(List records, Integer page, Integer size, String name){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getByName(name, page - 1, size)).withRel("previous");
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

    public String lastLinkName(List records, Integer page, Integer size, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);

        page = pageCount - 1;
        Link last = linkTo(methodOn(MainController.class).getByName(name, page , size)).withRel("last");

        return last.toString();
    }

    public String firstLinkName(List records, Integer page, Integer size, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getByName(name, page , size)).withRel("first");

        return first.toString();
    }

    public String nextLinkSupplier(List records, Integer page, Integer size, String ico, String name){
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getSupplier(ico, name, page + 1, size)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkSupplier(List records, Integer page, Integer size, String ico, String name){
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getSupplier(ico, name, page - 1, size)).withRel("previous");
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
        Link last = linkTo(methodOn(MainController.class).getSupplier(ico, name, page , size)).withRel("last");

        return last.toString();
    }

    public String firstLinkSupplier(List records, Integer page, Integer size, String ico, String name){

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = 1;
        Link first = linkTo(methodOn(MainController.class).getSupplier(ico, name, page , size)).withRel("first");

        return first.toString();
    }

    public String nextLinkTender(List records, Integer page, Integer size, Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name) throws ParseException{
        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        String nex = new String();
        if (page < pageCount -1) {
            Link next = linkTo(methodOn(MainController.class).getTender(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("next");
            nex = next.toString();
        }
        return nex;
    }

    public String prevLinkTender(List records, Integer page, Integer size,  Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name)throws ParseException{
        String prev = new String();
        if ((page -1) != 0) {
            Link previous = linkTo(methodOn(MainController.class).getTender(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("previous");
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
        Link last = linkTo(methodOn(MainController.class).getTender(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("last");

        return last.toString();
    }

    public String firstLinkTender(List records, Integer page, Integer size, Double volumeFrom, Double volumeTo, String dateFrom, String dateTo, String name)throws ParseException{

        int pageCount = (records.size() / size) + (records.size() % size > 0 ? 1 : 0);
        page = pageCount - pageCount + 1;
        Link first = linkTo(methodOn(MainController.class).getTender(name, dateFrom, dateTo, page, size, volumeFrom, volumeTo)).withRel("first");

        return first.toString();
    }
}
