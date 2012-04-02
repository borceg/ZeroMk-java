import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ZeroShortner{
	private String mApiKey;
	private byte[] mData;
	private String mUser;
	private ErrorListener mErrorListener;
	private int mOutput;

	public ZeroShortner(String apikey, String user)
	{
		/*
		 * @params apiKey - your 0.mk API key 
		 * @params user - your 0.mk username
		 * 
		 */
		mApiKey = apikey;
		mUser = user;
	}
	public ZeroShortner()
	{
	}
	public int getOutputFormat()
	{
		/*
		 * @return mOutput - the output format you have specified previously.
		 */
		return mOutput;
	}
	public String getApiKey()
	{
		/*
		 * @return mApiKey - the API key. If you have used parameterless constructor null value will be returned.
		 */
		return mApiKey;
	}
	
	public void setOnErrorListener(ErrorListener listener)
	{
		/*
		 * Error listener that will be envoked when the 0.mk returns greskaId and greskaMsg in the data. 
		 * Visit Constants for detailed information about errors.
		 * @params listener - ErrorListener interface
		 */
		mErrorListener = listener;
	}
	public void deleteLink(ZeroLink link)//not finished, needs more working
	{
		/*
		 * Deletes a Zero link object from 0.mk
		 * 
		 * @params link  - ZeroLink object on which deletion will be performed
		 * @throws NullPointerException - if Link object is null
		 */
		URL url;
		try {
			url = new URL(link.getDeleteLink());
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setConnectTimeout(30000);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ZeroLink previewLink(int format, String shortLink)
	{
		/*
		 * Method that will let you preview data associated with short link already baked with 0.mk
		 * 
		 * @params format - Format of the output from 0.mk API @see Constants
		 * @throws IllegalArgumentException - if the shortLink argument is null
		 * 
		 */
		if(shortLink==null)
			throw new IllegalArgumentException("link argumentot ne smee da bide null");
		URL url;
		try {
			url = new URL(buildMyUrl(Constants.PREVIEW_URL, shortLink, format, null));
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setConnectTimeout(30000);
			httpCon.setDoInput(true);
			byte[] data = getContentByteArray(httpCon.getInputStream());
			return getLinkObjectInternal(data, format);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null ;
		
	}
	public byte[] shortLink(String link, int output, String nastavka)
	{
		/*
		 * Method that is used to short your links using 0.mk API. This method can be used several times with the same instance and different parameters.
		 * @params link - Long link that you want to process.
		 * @params output - Output format of the response that you want be returned from the API.
		 * @params nastavka - The custom appendix of the end of the link. e.g http://0.mk/java 
		 * @throws IllegalArgumentException - if link parameter is null.
		 */
		mOutput = output;
		if(link==null)
			throw new IllegalArgumentException("link argumentot ne smee da bide null");
		URL url;
		try {
			url = new URL(buildMyUrl(Constants.SITE_URL, link, output, nastavka));
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setConnectTimeout(30000);
			httpCon.setDoInput(true);
			mData = getContentByteArray(httpCon.getInputStream());
			return  mData;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return null;
		
	}
	public InputStream getBytesForQr()
	{
	
		/*
		 * Used to create image representation of the QR code.
		 * @return - InputStream that later can be manipulated.
		 */
		if(mData==null)return null;
		return new ByteArrayInputStream(mData);
		
	}

	public ZeroLink getLinkObject()
	{
		/*
		 * This methods returns ZeroLink object with all data associated with, from the format you have specified (JSON, XML, PLAINTEXT).
		 * For QR code output use getBytesForQr() instead, otherwise null will be returned.
		 */
		return getLinkObjectInternal(mData, mOutput);
		
	}
	private ZeroLink getLinkObjectInternal(byte[] data, int outputformat)
	{
		
		ZeroLink link;
		switch(outputformat)
		{
		case Constants.JSON_FORMAT:
			link= Utils.parseJSON(getStringResponseInternal(data));
			break;
		case Constants.PLAIN_TEXT_FORMAT:
			link = new ZeroLink();
			link.setShortLinkUrl(getStringResponseInternal(data));
			break;
		case Constants.XML_FORMAT:
			link= Utils.parseXML(data);
			break;
		case Constants.QR_FORMAT:
		return null;
		default:
			link = Utils.parseJSON(getStringResponseInternal(data));
		}
		if(link.getErrorId()!= -1 && mErrorListener!=null)
			mErrorListener.onError(link.getErrorId(), link.getErrorMsg());
		return link;
	}
	private String getStringResponseInternal(byte[] data)
	{
		byte[] buffer = new byte[1024];
		int length = buffer.length;
		InputStream in = new ByteArrayInputStream(data);
		StringBuffer string = new StringBuffer();
		try {
			while((int)in.read(buffer, 0, length) > 0)
			{
				string.append(new String(buffer, 0, length));
			}
			String response = string.toString();
			
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getStringResponse()
	{
		return getStringResponseInternal(mData);
	}
	private String buildMyUrl(String apilink, String link, int out, String nastavka)
	{
		
		StringBuffer urlBuilder = new StringBuffer();
		urlBuilder.append(apilink);
		urlBuilder.append("format=").append(out==Constants.JSON_FORMAT? "json" : 
		out==Constants.PLAIN_TEXT_FORMAT? "plaintext" : out==Constants.XML_FORMAT? "xml"
		: out == Constants.QR_FORMAT ? "qr" : "json");
		if(mApiKey!=null && mUser!=null)
			urlBuilder.append("&korisnik=").append(mUser).append("&apikey=").append(mApiKey);
		if(nastavka!=null && nastavka.length()>0)
			urlBuilder.append("&nastavka=").append(nastavka);
		urlBuilder.append("&link=").append(link);
		return urlBuilder.toString();
		
	}
	private byte[] getContentByteArray(InputStream in)
	{
		ByteArrayOutputStream btemp = new ByteArrayOutputStream();
		int dataByte;
		try {
			dataByte = in.read();
			while(dataByte > -1)
			{
				btemp.write(dataByte);
				dataByte = in.read();
			}
			in.close();
			return btemp.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
}

		
