package vnskilled.edu.ecom.Model.Response.Address;

import vnskilled.edu.ecom.Model.DTO.Address.AddressWardsDTO;

import java.util.ArrayList;
import java.util.List;

public class AddressWardOutPut {
    private int page;
    private int totalPage;
    private List<AddressWardsDTO> listResult = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<AddressWardsDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<AddressWardsDTO> listResult) {
        this.listResult = listResult;
    }
}
