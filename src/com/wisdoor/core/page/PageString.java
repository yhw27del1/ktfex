package com.wisdoor.core.page;

public class PageString
{
	
	public static String getPage(int start, int range, long count)

	{  
		// 总页数
		long numPages = count / range + (0 == count % range ? 0 : 1);

		if (numPages <= 1)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append("<div class=\"page_css\">");

		// 当前处于第几页
		int currentPage = start / range + 1; 
		
		if (currentPage == 1)
		{
           sb.append("<span class=\"disabled\"> &lt; </span>");

		}else{
			sb.append("<a href=\"#\" onclick=\"prePage()\"");
			sb.append("\">&lt; "); 
			sb.append("</a>");  
		}


		int low = currentPage - 5;

		if (low <= 0)
		{
			low = 1;
		}

		int high = currentPage + 5;

		// 加上...
		if (low > 2)
		{
			sb.append("<a href=\"#");
			sb.append("\">");
			sb.append("1");
			sb.append("</a>");
			sb.append("...");
		}

		while (low < currentPage)
		{
			  
			sb.append("<a href=\"#\" onclick=\"toPage("+low+")\"");
			sb.append("\">");
			sb.append(low);
			sb.append("</a>");  
			low++;
		}

		
		
		// 打印当前页
		sb.append("<span class=\"current\">");
		sb.append(currentPage);
		sb.append("</span>");

		// 打印当前页的后5页

		currentPage++;

		while ((currentPage <= high) && (currentPage <= numPages))
		{ 
			sb.append("<a href=\"#\" onclick=\"toPage("+currentPage+")\"");
			sb.append("\">");
			sb.append(currentPage); 
			sb.append("</a>");  
			currentPage++;
		}

		if (high + 1 < numPages)
		{
			sb.append("...");
		}

		if (high + 1 <numPages)
		{  
			sb.append("<a href=\"#\" onclick=\"toPage("+numPages+")\"");
			sb.append("\">");
			sb.append(numPages);
			sb.append("</a>"); 
		}
 
		if ((start / range + 1)== numPages)  
		{
           sb.append("<span class=\"disabled\">&gt;</span>");

		}else{
			sb.append("<a href=\"#\" onclick=\"nextPage()\"");
			sb.append("\"> &gt; </a>"); 
		}

		sb.append("</div>");

		return sb.toString();
	}
}

