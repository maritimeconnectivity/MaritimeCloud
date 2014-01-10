/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.maritimecloud.internal.net.server.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * The default exception handler
 * 
 * @author Kasper Nielsen
 */
@Provider
public class DmaExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException e) {

        // get initial response
        Response response = e.getResponse();

        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\"/>\n");
        sb.append("<title>Error 404 </title>\n");
        sb.append("</head>\n");

        sb.append("<body>\n");
        sb.append("<h2>HTTP ERROR: 404</h2>\n");
        sb.append("<p>Problem accessing /hello. Reason:\n");
        sb.append("<pre>    " + e.getMessage() + "</pre></p>\n");
        sb.append("<hr /><i><small>Brought to you by Danish Maritime Authority!!!!</small></i>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return Response.status(response.getStatus()).entity(sb.toString()).build();
    }
}
