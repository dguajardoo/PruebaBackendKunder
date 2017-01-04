/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author dguaj
 */
@Stateless
@Path("service")
public class GenericResource {

    @Context
    private UriInfo context;
    private static int status_200 = 200;
    private static int status_400 = 400;
    private static int status_500 = 500;


    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    @Path("/word")
    public Response postMsg(@FormParam("data") String data) {
        
        if (data == null){
            return generarJSON("", true, status_500);
        } else {
            String cadena = data;
            char[] arrayChar = cadena.toCharArray();
            int cantidad = arrayChar.length;

            if (cantidad == 4) {
                cadena = cadena.toUpperCase();
                return generarJSON(cadena, true, status_200);
            } else {
                return generarJSON(cadena, false, status_400);
            }   
        }
        
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/time")
    public Response getTime(@QueryParam("value") String value) { 
        
        if (value == null){
            return generarJSON("", true, status_500);
        } else {
            //DateFormat df = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss.s'TZD'");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZZZZ");

            String nowAsISO = "";
            try{
                if (validaHora(value)) {
                    Date d = sdf.parse(value);
                    nowAsISO = output.format(d);
                    return generarJSON(nowAsISO, true, status_200);
                } else {
                    return generarJSON(nowAsISO, false, status_400);
                }
            } catch(Exception e){
                return generarJSON(e.getMessage(), false, status_400);
            }
        }
    }
    
    public boolean validaHora(String hora){
        char[] arrayChar = hora.toCharArray();
        int cantidad = arrayChar.length;
        if (cantidad <=5 && cantidad>0) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            try{
                sdf.parse(hora);
            }catch(ParseException e){
                return false;
            }
        } else {
            return false;
        }
        
        return true;
    }

    public Response generarJSON(String cadena, boolean flag, int status) {
        JSONObject obj = new JSONObject();
        if (flag && status == 200) {
            status = 200;
            obj.put("code", "00");
            obj.put("description", "OK");
            obj.put("data", cadena);
        } else if (flag == false && status == 400){
            status = 400;
            obj.put("code", "01");
            obj.put("description", "400 Bad Request.");
            obj.put("data", "NO CUMPLE LO SOLICITADO.");
        } else {
            status = 500;
            obj.put("code", "02");
            obj.put("description", "500 Internal Server Error.");
            obj.put("data", "FALTA INFORMACIÓN.");
        }
        
        return Response.status(status).entity(obj).build();
    }
}
