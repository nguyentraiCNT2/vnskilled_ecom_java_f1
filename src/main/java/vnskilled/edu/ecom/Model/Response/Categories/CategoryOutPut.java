package vnskilled.edu.ecom.Model.Response.Categories;

import java.util.ArrayList;
import java.util.List;

public class CategoryOutPut {
	private int page;
	private int totalPage;
	private List<CategoryResponse> listResult = new ArrayList<> ();

	public int getPage () {
		return page;
	}

	public void setPage (int page) {
		this.page = page;
	}

	public int getTotalPage () {
		return totalPage;
	}

	public void setTotalPage (int totalPage) {
		this.totalPage = totalPage;
	}

	public List<CategoryResponse> getListResult () {
		return listResult;
	}

	public void setListResult (List<CategoryResponse> listResult) {
		this.listResult = listResult;
	}
}
