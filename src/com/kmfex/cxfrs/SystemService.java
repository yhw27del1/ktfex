package com.kmfex.cxfrs;

import java.io.Serializable;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kmfex.cxfrs.vo.MessageTip;



@Path(value="/systemService")
@Produces(MediaType.APPLICATION_JSON)
public interface SystemService extends Serializable {
		
	 @GET
	 @Path("/getServerTime")
	 String getServerTime();


	 @GET
	 @Path("/login/{un}/{up}/{ut}")
     MessageTip login(@PathParam("un")String un, @PathParam("up")String up, @PathParam("ut")String ut);
	 
	 @GET
	 @Path("/updatePassword/{un}/{ut}/{op}/{np}")
	 MessageTip updatePassword(@PathParam("un")String un, @PathParam("ut")String ut, @PathParam("op")String op, @PathParam("np")String  np);

}
