package vnskilled.edu.ecom.Model.Response.Address;

import vnskilled.edu.ecom.Model.DTO.Address.AddressCountryDTO;

import java.util.ArrayList;
import java.util.List;

public class AddressCountryOutPut {
    private int page;
    private int totalPage;
    private List<AddressCountryDTO> listResult = new ArrayList<>();

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

    public List<AddressCountryDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<AddressCountryDTO> listResult) {
        this.listResult = listResult;
    }
}
