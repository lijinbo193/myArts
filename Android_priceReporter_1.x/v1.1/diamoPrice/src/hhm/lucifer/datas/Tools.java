package hhm.lucifer.datas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.Locale;

import hhm.lucifer.activity.DiamoPriceActivity;
import jxl.Sheet;

public class Tools 
{   
	/***
	 * ���DiamoPriceActivity����
	 * @param dpa
	 */
	public static boolean initial(DiamoPriceActivity dpa)
	{
		boolean b=false;
		//newÿһ��CurrentPriceForms����,��Ϊ֮�趨���
		for(int i=0;i<dpa.getCurrentPriceForms().length;i++)
		{
			dpa.getCurrentPriceForms()[i]=new CurrentPriceForms();
			dpa.getCurrentPriceForms()[i].setFormNum(i);
		}
		//����ReadFromFiles����
		ReadFromFiles rff=new ReadFromFiles();
		//��xls�ļ���·����Sheet�ı�ŵõ�Sheet
        Sheet st=rff.getSheetFromEcxel(dpa.getParas()[0],dpa.getParas()[1]);
		//��18�ű����CurrentPriceForms��������
		if(rff.fillInCurrentPriceForms(st, dpa.getCurrentPriceForms()))
			b=true;
		rff.close();  //�ر�������
		return b;
	}
	
	
	/***
	 * �������ļ��л�ȡ3������
	 * @return
	 */
	public static String[] getIniParameters(String[] paras)
	{
		String[] parameters=new String[3];
		String temp;
	    File file;
	    BufferedReader source;
	    try{
	      file=new File("/mnt/sdcard/diamondCfg/config.txt");
	    	   source=new BufferedReader(new FileReader(file));
	    	   while((temp=source.readLine())!=null)
	    	   {
	    		   //����п��У���������#�ſ�ͷ�������
	    		  if(temp.equals("")||temp.subSequence(0, 1).equals("#"))
	    			  continue;
	    		  //System.out.println(temp.trim());  //ȥ��ǰ��Ŀո�
	    		  String[] multiStr=temp.split("=");  //���������ɡ�=���ŷֿ�
	    		  String[] parameterStr=multiStr[1].split("\"");  //ȡ��˫�����������Ĳ���
	    		  
	    	      if(multiStr[0].equals("filePath"))
	    	    	  parameters[0]=parameterStr[1];
	    	      else if(multiStr[0].equals("sheetNum"))
	    	    	  parameters[1]=parameterStr[1];
	    	      else if(multiStr[0].equals("exchangeRate"))
	    	    	  parameters[2]=parameterStr[1];	
	    	      
	    	      for(int i=0;i<parameters.length;i++)
	    	    	  paras[i]=parameters[i];
	    	   }
	       }
	       catch(Exception e)
	       {
	    	   e.printStackTrace();
	       }
		return parameters;
	}
	
	/***
	 * �޸Ļ���
	 * @param exchangeRate �»���
	 * @return �޸ĳɹ��򷵻�true
	 */
	public static boolean changeExchangeRate(String exchangeRate)
	{
		boolean b=false;
		
		String cfgFilePath="/mnt/sdcard/diamondCfg/config.txt";  //�����ļ�
		String tempFilePath="/mnt/sdcard/diamondCfg/temp.txt";  //��ʱ�ļ�
		String temp="";
		File file=new File(cfgFilePath);
		File tempFile=new File(tempFilePath);
		BufferedReader source;
		BufferedWriter target;
		try{
			 source=new BufferedReader(new FileReader(file));
			 target=new BufferedWriter(new FileWriter(tempFile));
			 
			 while((temp=source.readLine())!=null)
			 {
				//����п��У���������#�ſ�ͷ�������
	    		  if(temp.equals("")||temp.subSequence(0, 1).equals("#"))
	    		  {
				      target.write(temp);   //�����ļ�
				      target.newLine();
				      target.flush();
	    			  continue;
	    		  }
	    		  //System.out.println(temp.trim());  //ȥ��ǰ��Ŀո�
	    		  String[] multiStr=temp.split("=");  //���������ɡ�=���ŷֿ�
	    	
	    	    if(multiStr[0].equals("exchangeRate"))
	    		   temp="exchangeRate="+"\""+exchangeRate+"\"";	    	

	    	      target.write(temp);  //�����ļ�
			      target.newLine();
			      target.flush();
			 }
			 //������ر��������������޷�ɾ��ԭ����config.txt
			 source.close();
			 target.close();
			 //ɾȥԭ����config.txt,��tempFile����Ϊcofig.txt
			 file.delete();
			 tempFile.renameTo(new File("/mnt/sdcard/diamondCfg/config.txt"));
			 b=true;
		}catch(Exception e){
		  e.printStackTrace();
		}
		return b;
	}
	
	/***
	 * 
	 * @param money
	 * @param country
	 * @return ��Ӧ����Ǯ����ʾ
	 */
	public static String getCurrencyFormat(double money,String country)
	{
		 String theRightFormat="";
		 NumberFormat chinaFormat=NumberFormat.getCurrencyInstance(Locale.CHINA);
		 NumberFormat amerivanFormat=NumberFormat.getCurrencyInstance(Locale.US);
		 if(country.equals("china"))
			 theRightFormat=chinaFormat.format(money);
		 else
			 theRightFormat=amerivanFormat.format(money);
		 return theRightFormat;
	}
	
	/**
	 * @category ��String�Ƿ���ת��Ϊdouble
	 * @return ���򷵻�true ���򷵻�false
	 * */
	public static boolean rateIsDouble(String rateBeSet) {
		boolean b = false;
		try {
			double newRate = Double.parseDouble(rateBeSet);
			b = true;
			return b;
		} catch (NumberFormatException e) {
			return b;
		}
	}
	
	/***
	 * ��������weight�Ƿ�Ϸ�
	 * @param weight
	 * @return �Ϸ���true
	 */
	public static boolean checkWeight(String weight)
	{  
		//1���ȿ��ܲ���double�������򲻺Ϸ�
		//2������Ϊ4����ֻ����0.01--5.99֮��
		//3������Ϊ5����ֻ����10.00--10.99֮��
		boolean b=false;
		b=Tools.rateIsDouble(weight);  //��������ĺ���
		if(b)
		{
			b=false;
			//�������double��,��һ������
			double value=Double.parseDouble(weight);
			if((0.01<=value&&value<=5.99)||(10.00<=value&&value<=10.99))
				b=true;
		}//end if
		return b;
	}//end function
	
	/***
	 * ���ݱ�ʯ�����������۸��ı��
	 * @param weight
	 * @return
	 */
	public static int getFormNumByWeight(double weight)
	{
		int formNum=0;   //��Ϊ0���������д���
		   
		   if(0.01<=weight&&weight<=0.03)
			   formNum=1;
		   else if(0.04<=weight&&weight<=0.07)
			   formNum=2;
		   else if(0.04<=weight&&weight<=0.07)
			   formNum=2;
		   else if(0.08<=weight&&weight<=0.14)
			   formNum=3;
		   else if(0.15<=weight&&weight<=0.17)
			   formNum=4;
		   else if(0.18<=weight&&weight<=0.22)
			   formNum=5;
		   else if(0.23<=weight&&weight<=0.29)
			   formNum=6;
		   else if(0.30<=weight&&weight<=0.39)
			   formNum=7;
		   else if(0.40<=weight&&weight<=0.49)
			   formNum=8;
		   else if(0.50<=weight&&weight<=0.69)
			   formNum=9;
		   else if(0.70<=weight&&weight<=0.89)
			   formNum=10;
		   else if(0.90<=weight&&weight<=0.99)
			   formNum=11;
		   else if(1.00<=weight&&weight<=1.49)
			   formNum=12;
		   else if(1.50<=weight&&weight<=1.99)
			   formNum=13;
		   else if(2.00<=weight&&weight<=2.99)
			   formNum=14;
		   else if(3.00<=weight&&weight<=3.99)
			   formNum=15;
		   else if(4.00<=weight&&weight<=4.99)
			   formNum=16;
		   else if(5.00<=weight&&weight<=5.99)
			   formNum=17;
		   else if(10.00<=weight&&weight<=10.99)
			   formNum=18;
		   else ;
		 
		   return formNum;
	}
	
	/**
	 * @category �ж�·������ʾ���ļ���ʽ�Ƿ���.xls
	 * */
	public static boolean isFileFormateRight(String filePath) {
		boolean b = false;
		String thePath = filePath; //��ȡtxtExcelPath�ϵ�Excel�ļ�·��
		int thePathLength = thePath.length(); //�õ��ļ�����
		String theExtension = thePath.substring(thePathLength - 3,thePathLength); //�õ���չ��
		if (theExtension.equals("xls")) {
			b = true;
			//this.showWarningDialog("��չ����:" + theExtension);
		}
		return b;
	}
	
	public static boolean checkSheetName(String sheetName)
	{
		boolean b=true;
		//���sheetName��ǰ5����ĸ����Sheet�����µĲ��������������
        int length=sheetName.length();
        String fisrtFive=sheetName.substring(0, 5);
        String theRest=sheetName.substring(5, length);
        try{
        	if(!fisrtFive.equals("Sheet"))
        		b=false;
        	int toInt=Integer.valueOf(theRest);  //����Ҫ�ã�ֻ������׽Ī���еĴ���
        	return b;
        }
        catch(Exception e)
        {
        	b=false;
        	return b;
        }
	}
}//end class Tools
