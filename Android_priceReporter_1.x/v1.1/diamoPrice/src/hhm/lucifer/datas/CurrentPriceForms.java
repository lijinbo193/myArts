package hhm.lucifer.datas;

public class CurrentPriceForms 
{
	private int formNum;  //��ǰҪʹ�ñ���
	   
	   private int lightRows=5;  //ǰ���ű������
	   private int lightCols=8;  //ǰ���ű������
	 //double�Ͷ�ά���飬ǰ���ŵ�,5��8��
	   private double[][] light_PriceForm=new double[lightRows][lightCols];
	   
	   private int weightRows=10;  //��ʮ���ű������
	   private int weightCols=11;  //��ʮ���ű������
	 //double�Ͷ�ά����,��ʮ���ŵģ�10��11��
	   private double[][] weight_PriceForm=new double[weightRows][weightCols];
	    
	   public int getFormNum() {
		return formNum;
	}
	public void setFormNum(int formNum) {
		this.formNum = formNum;
	}
	public int getLightRows() {
		return lightRows;
	}
	public void setLightRows(int lightRows) {
		this.lightRows = lightRows;
	}
	public int getLightCols() {
		return lightCols;
	}
	public void setLightCols(int lightCols) {
		this.lightCols = lightCols;
	}
	public double[][] getLight_PriceForm() {
		return light_PriceForm;
	}
	public void setLight_PriceForm(double[][] lightPriceForm) {
		for(int row=0;row<lightRows;row++)
		   {
			   for(int col=0;col<lightCols;col++)
			   {
	          this.light_PriceForm[row][col]=lightPriceForm[row][col];
	          //System.out.println("here setMid_PriceForm:"+mid_PriceForm[row][col]);
			   }
		   }
	}
	public int getWeightRows() {
		return weightRows;
	}
	public void setWeightRows(int weightRows) {
		this.weightRows = weightRows;
	}
	public int getWeightCols() {
		return weightCols;
	}
	public void setWeightCols(int weightCols) {
		this.weightCols = weightCols;
	}
	public double[][] getWeight_PriceForm() {
		return weight_PriceForm;
	}
	public void setWeight_PriceForm(double[][] weightPriceForm) {
		for(int row=0;row<weightRows;row++)
		   {
			   for(int col=0;col<weightCols;col++)
	          this.weight_PriceForm[row][col]=weightPriceForm[row][col];
		   }
	}

	//************************���������ݳ�Ա******************************************   
	/***
	 * ����ÿ�ű�ı��
	 * @param num
	 */
	public void setPriceFormNum(int num)
	{
		this.setFormNum(num);
	}

	/***
	 * ���ݱ�ı����ʾÿ�ű�
	 * @param num
	 */
	public String showFormByNum()
	{
		int num=formNum;
		String form="";
		if(num>=1&&num<=18)//����ǵ�1--18�ű�
		{
			//�����ǰ6�ű�
			if(num<=6)
			{
				for(int i=0;i<lightRows;i++)
				{
					for(int j=0;j<lightCols;j++)
                       form+=light_PriceForm[i][j]+" ";
					form+="\n";
				}
			}
			//����Ǻ�12�ű�
			if(num>6)
			{
				for(int i=0;i<weightRows;i++)
				{
					for(int j=0;j<weightCols;j++)
                       form+=weight_PriceForm[i][j]+" ";
					form+="\n";
				}
			}
		}//end if
		return form;
	}//end showForm
	
}//end  class CurrentPriceForms
