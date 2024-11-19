package vnskilled.edu.ecom.Model.Response.Address;

import vnskilled.edu.ecom.Model.DTO.Address.AddressCityDTO;

import java.util.ArrayList;
import java.util.List;

public class AddressCityOutPut {
    private int page;
    private int totalPage;
    private List<AddressCityDTO> listResult = new ArrayList<>();

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

    public List<AddressCityDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<AddressCityDTO> listResult) {
        this.listResult = listResult;
    }
}
