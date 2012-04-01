import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;



import org.json.JSONException;
import org.json.JSONObject;

public class Utils 
	{
		public static ZeroLink parseJSON(String response)
		{
			
			ZeroLink lnk = new ZeroLink();
			try {
				JSONObject o = new JSONObject(response);
				lnk.setStatus(o.getInt("status"));
				lnk.setLongLinkUrl(o.getString("dolg"));
				lnk.setErrorId(o.getString("greskaId").equals("")? -1 : o.getInt("greskaId"));
				lnk.setErrorMsg(o.getString("greskaMsg"));
				lnk.setUrlTitle(o.getString("urlNaslov"));
				lnk.setShortLinkUrl(o.getString("kratok"));
				lnk.setAppendix(o.getString("nastavka"));
				lnk.setDeleteCode(o.getString("brisiKod"));
				lnk.setStatisticsLink(o.getString("statsLink"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return lnk;
		}
		public static ZeroLink parseXML(byte[]data)
		{
			
			ZeroLink link = null;
			try {
				XMLInputFactory xmlif = XMLInputFactory.newInstance();
				InputStream input = new ByteArrayInputStream(data);
				XMLEventReader r = xmlif.createXMLEventReader(input);
				
				while(r.hasNext())
				{
					 XMLEvent event = r.nextEvent();
					if(event.getEventType()==XMLStreamConstants.START_ELEMENT  )
					{ 	StartElement startElement = event.asStartElement();
						String temp = startElement.getName().getLocalPart();
						if (temp.equalsIgnoreCase("http0mk")){
		     		    	link = new ZeroLink();
		                 } else if (link != null){
		                     if (temp.equals("kratok")){
		                  link.setShortLinkUrl(r.getElementText());
		                 } else if (temp.equals("kratok")){
		                	
		                 }else if (temp.equals("dolg")){
		                	 link.setLongLinkUrl(r.getElementText());
		                 }else if (temp.equals("urlNaslov")){
		                	 link.setUrlTitle(r.getElementText());
		                 }else if (temp.equals("nastavka")){
		                	 link.setAppendix(r.getElementText());
		                 }else if (temp.equals("status")){
		                	 link.setStatus(Integer.parseInt(r.getElementText()));
		                 }else if (temp.equals("greskaId")){
		                	 link.setErrorId(Integer.parseInt(r.getElementText()));
		                 }else if (temp.equals("greskaMsg")){
		                	 link.setErrorMsg(r.getElementText());
		                 }else if (temp.equals("brisiLink")){
		                	 link.setDeleteLink(r.getElementText());
		                 }else if (temp.equals("brisiKod")){
		                	 link.setDeleteCode(r.getElementText());
		                 }else if (temp.equals("statsLink")){
		                	 link.setStatisticsLink(r.getElementText());
		                 }
		                 }
					}
				}
			}catch (XMLStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return link;
					
				
		}
	
		/*static ZeroLink parseXMLpull(byte[] data)
		{
			ZeroLink link = null ;
		
			XmlPullParser parser = null;
	 		InputStream in = new ByteArrayInputStream(data);
	 	    	try {	 
	 	    		parser = XmlPullParserFactory.newInstance().newPullParser();
	 	    		Reader r = new InputStreamReader(in);
					parser.setInput(r);
				} catch (XmlPullParserException e1) {
				
					e1.printStackTrace();
				}
	 		int type=0;
	 		try {
	 			type = parser.getEventType();
	 		} catch (XmlPullParserException e) {
	 			e.printStackTrace();
	 		}
	 		String temp = null;

	 		boolean done = false;
	 		try {
	 		
	     	while(type!=XmlPullParser.END_DOCUMENT && !done)
	     	{
	     		switch(type)
	     		{
	     		case XmlPullParser.START_DOCUMENT:
	     			break;
	     		case XmlPullParser.START_TAG:
	     			temp = parser.getName();
	     		    if (temp.equalsIgnoreCase("http0mk")){
	     		    	link = new ZeroLink();
	                 } else if (link != null){
	                     if (temp.equals("kratok")){
	                  link.setShortLinkUrl(parser.readText());
	                 } else if (temp.equals("kratok")){
	                	
	                 }else if (temp.equals("dolg")){
	                	 link.setLongLinkUrl(parser.readText());
	                 }else if (temp.equals("urlNaslov")){
	                	 link.setUrlTitle(parser.readText());
	                 }else if (temp.equals("nastavka")){
	                	 link.setAppendix(parser.readText());
	                 }else if (temp.equals("status")){
	                	 link.setStatus(Integer.parseInt(parser.readText()));
	                 }else if (temp.equals("greskaId")){
	                	 link.setErrorId(Integer.parseInt(parser.readText()));
	                 }else if (temp.equals("greskaMsg")){
	                	 link.setErrorMsg(parser.readText());
	                 }else if (temp.equals("brisiLink")){
	                	 link.setDeleteLink(parser.readText());
	                 }else if (temp.equals("brisiKod")){
	                	 link.setDeleteCode(parser.readText());
	                 }else if (temp.equals("statsLink")){
	                	 link.setStatisticsLink(parser.readText());
	                 }
	                 break;
	                 }
	     	       case XmlPullParser.END_TAG:
	                	temp = parser.getName();
	                    
	                	if (temp.equalsIgnoreCase("http0mk"))
	                     {
	                    	
	                        done = true;
	                     
	                    }
	                    break;
	                    
	            }
	     		  type = parser.next();
	     		}
	     	in.close();
	 		} catch (XmlPullParserException e) {
	 		
	 			e.printStackTrace();
	 		} catch (Exception e) {

	 			e.printStackTrace();
	 		} 
	 	
	 	

			return link;
	 		}*/
	}