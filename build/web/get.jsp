<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>
<%
        response.setContentType("application/json");
            
        String json = "";
        
        
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
        
        
        
        out.print(json);
                
                
        
        
    
%>