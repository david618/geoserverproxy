/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author david
 */
public class GetFeatures extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
            
        String json = "";
        
//        String postBody = "<wfs:GetFeature service=\"WFS\" version=\"1.1.0\"" +
////"  resultType=\"hits\"\n" +
//"  outputFormat=\"json\"" +
//"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
//"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
//"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
//"  xsi:schemaLocation=\"http://www.opengis.net/wfs\n" +
//"                      http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
//"  <wfs:Query typeName=\"Earthquakes:STL_LAX\">" +
//"    <ogc:Filter>" +
//"<ogc:And>" +
//"  <ogc:PropertyIsGreaterThanOrEqualTo>" +
//"   <ogc:PropertyName>DTG2</ogc:PropertyName>" +
//"   <ogc:Literal>2016-09-21 00:00:00</ogc:Literal>" +
//"  </ogc:PropertyIsGreaterThanOrEqualTo>" +
//"  <ogc:PropertyIsLessThan>" +
//"   <ogc:PropertyName>DTG2</ogc:PropertyName>" +
//"   <ogc:Literal>2016-09-22 00:00:00</ogc:Literal>" +
//"  </ogc:PropertyIsLessThan>" +
//"</ogc:And>" +
//"    </ogc:Filter>" +
//"    </wfs:Query>" +
//"</wfs:GetFeature>";
        
        String postBody = "<wfs:GetFeature service=\"WFS\" version=\"1.1.0\"" +
//"  resultType=\"hits\"\n" +
"  outputFormat=\"json\"" +
"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
"  xsi:schemaLocation=\"http://www.opengis.net/wfs\n" +
"                      http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
"  <wfs:Query typeName=\"Earthquakes:STL_LAX\">" +
"    <ogc:Filter>" +
"  <ogc:PropertyIsGreaterThanOrEqualTo>" +
"   <ogc:PropertyName>DTG2</ogc:PropertyName>" +
"   <ogc:Literal>ZZZZZ</ogc:Literal>" +
"  </ogc:PropertyIsGreaterThanOrEqualTo>" +
"    </ogc:Filter>" +
"    </wfs:Query>" +
"</wfs:GetFeature>";        
        
        try {
            
            String url = "http://192.168.56.101:8080/geoserver/Earthquakes/ows?service=WFS&version=1.0.0&request=GetFeature";
            
            URL obj = new URL(url);
            
            
            String pastseconds = request.getParameter("pastseconds");
            
            Integer seconds = 86400;  // default to day
            
            if (pastseconds != null) {
                seconds = Integer.parseInt(pastseconds);
            }
            
            
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/xml");
            
            
            Calendar cal = Calendar.getInstance();
            
            
            Date dt = new Date();
            cal.setTime(dt);
            
            cal.add(Calendar.SECOND, -seconds);
            
            dt = cal.getTime();
            
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String dtg = sdf.format(dt);
            System.out.println(dtg);
            
            postBody = postBody.replaceFirst("ZZZZZ", dtg);
            System.out.println(postBody);            
            
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream((con.getOutputStream()));
            wr.writeBytes(postBody);
            wr.flush();
            wr.close();
            
            
            

            
            int responseCode = con.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            
            String line;
            StringBuffer rsp = new StringBuffer();
            
            while ((line = in.readLine()) != null) {
                rsp.append(line);
            }
            
            in.close();
            
            json = rsp.toString();
            
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null) msg = e.getClass().getName();
            json = "{\"error\":\"" + msg + "\"}";
        } 
        
        PrintWriter out = response.getWriter();
        
        out.print(json);
                
                
        
        
        
        
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet GetFeatures</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet GetFeatures at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//        
        
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
