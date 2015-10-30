package hhm.lucifer.datas;

import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class ReadFromFiles 
{
	private int sheetRows;  //��ǰ��ȡ��sheet������,�ݲ���
	private int sheetCols;  //��ǰ��ȡ��sheet���������ݲ���
	private String sheetName;  //��ǰ��ȡ��sheet�����֣��ݲ���
	private InputStream is;  //������
	private Workbook wb;  //����һ��������
	
	
	public Workbook getWb() {
		return wb;
	}

	public void setWb(Workbook wb) {
		this.wb = wb;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getSheetRows() {
		return sheetRows;
	}

	public void setSheetRows(int sheetRows) {
		this.sheetRows = sheetRows;
	}

	public int getSheetCols() {
		return sheetCols;
	}

	public void setSheetCols(int sheetCols) {
		this.sheetCols = sheetCols;
	}

	//*****************************���������ݳ�Ա*********************************************

    /**
     * 
     * @param excelFilrPath ��Ҫ��ȡ��excel�ļ���·��
     * @param sheetNum  ��Ҫ��ȡ��Sheet���
     * @return
     */
	public Sheet getSheetFromEcxel(String excelFilePath,String sheetNum)
	{
		Sheet sheetBeRead=null;
		try{
			is=new FileInputStream(excelFilePath);
			//ȡ�ù�����
			wb=Workbook.getWorkbook(is);
			//��ù������ĸ���
			wb.getNumberOfSheets();
			//��ȡ����Ҫ��Sheet
			sheetBeRead=wb.getSheet(sheetNum);
			//�õ���Sheet�������Ϣ
			this.setSheetName(sheetBeRead.getName());  //�õ�Sheet������
			this.setSheetRows(sheetBeRead.getRows());  //�õ�Sheet������
			this.setSheetCols(sheetBeRead.getColumns());  //�õ�Sheet������
			//st.getCell(arg0, arg1);  //getCell(cols,row);
			//System.out.println(this.getSheetName()+" \n�У�"+this.getSheetRows()+" \n��:"+this.getSheetCols());
			return sheetBeRead;
		}
		catch(Exception e)
		{
			return sheetBeRead;
		}
	}// end getSheetFormExcel()
	
	public boolean fillInCurrentPriceForms(Sheet st,CurrentPriceForms[] currentPriceForms)
	{
//		for(int i=1;i<19;i++)
//		System.out.print(currentPriceForms[i].getFormNum()+"\t");
//		System.out.print("here is fillInCurrentPriceForms\n");
//		System.out.println(st.getName()+" \n�У�"+st.getRows()+" \n��:"+st.getColumns());
		//System.out.println("here fillInCurrentPriceForms()"+st.getCell(1,2).getContents());
		boolean b=true;
		int weightRows=currentPriceForms[0].getWeightRows();  //10
		int weightCols=currentPriceForms[0].getWeightCols();  //11
		int lightRows=currentPriceForms[0].getLightRows();  //5
		int lingtCols=currentPriceForms[0].getLightCols();  //8
		
		try{

			//����2��10��11�еĿ�2ά����
			   double[][] weightFormObj1=new double[weightRows][weightCols];
			   double[][] weightFormObj2=new double[weightRows][weightCols];
			 //����2��5��8�еĿ��н�2ά����'
			   double[][] lightFormObj1=new double[lightRows][lingtCols];
			   double[][] lightFormObj2=new double[lightRows][lingtCols];
			   
			   int formCurrentNumber=1;  //��¼��ǰ����ʼ���ı�ı��
			   int rowStart=2;
			   int rowEnd=6;
			   int colStart=1;
			   int colEnd=8;
	         //��������ĳ�ʼ������
			 //ǰ6�ű�
			   for(int count1=0;count1<3;count1++)
			   {
				 int downAdd=8;  //ÿѭ��һ������+8����λ
				 int leftAdd=13;  
				 for(int row=rowStart;row<=rowEnd;row++)
				 {
					 for(int col=colStart;col<=colEnd;col++)
					 {
						 String valueStr=st.getCell(col, row).getContents();
						// System.out.println("ff:"+st.getCell(col, row).getContents());
						 lightFormObj1[row-rowStart][col-colStart]=Double.parseDouble(valueStr);
						// System.out.println("ff:"+midFormObj1[row-rowStart][col-colStart]);
					 }
					 for(int col=colStart+leftAdd;col<=colEnd+leftAdd;col++)
					 {
						 String valueStr=st.getCell(col, row).getContents();
						 lightFormObj2[row-rowStart][col-colStart-leftAdd]=Double.parseDouble(valueStr);
					 }
					 //ĳһ�ŵ����ű��Ѿ���ʼ�����
				 }
				 System.out.println("here 1");

				 currentPriceForms[formCurrentNumber].setLight_PriceForm(lightFormObj1);  //��ʼ��light_PriceFrom			
				 currentPriceForms[formCurrentNumber+1].setLight_PriceForm(lightFormObj2);
				 formCurrentNumber+=2;  //�´�Ҫ��ʼ���ı�ı��
				 rowStart+=downAdd;  //����8��
				 rowEnd+=downAdd;
			   }//end for count
			   
			   //��12�ű�
			   rowStart=26;
			   rowEnd=35;
			   colStart=1;
			   colEnd=11;
			   for(int count2=0;count2<6;count2++)
			   {
				   int downAdd=13;  //ÿѭ��һ������+13����λ
				   int leftAdd=13;  
				   for(int row=rowStart;row<=rowEnd;row++)
				   {
					   for(int col=colStart;col<=colEnd;col++)
					   {
						   String valueStr=st.getCell(col, row).getContents();
						   weightFormObj1[row-rowStart][col-colStart]=Double.parseDouble(valueStr);
					   }
					   for(int col=colStart+leftAdd;col<=colEnd+leftAdd;col++)
					   {
						   String valueStr=st.getCell(col, row).getContents();
						   weightFormObj2[row-rowStart][col-colStart-leftAdd]=Double.parseDouble(valueStr);
					   }
				   }//end for row
				  
				   currentPriceForms[formCurrentNumber].setWeight_PriceForm(weightFormObj1);
				   currentPriceForms[formCurrentNumber+1].setWeight_PriceForm(weightFormObj2);
				   formCurrentNumber+=2;
				   rowStart+=downAdd;
				   rowEnd+=downAdd;
			   }//end for count
			   
		}
		catch(Exception e)
		{
			e.printStackTrace();
			b=false;
		}
	    return b;
	}
	
    public void close()
    {
    	if(this.getWb()!=null)
    	{
    		try{
    			this.getWb().close();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    }//end close()
}
