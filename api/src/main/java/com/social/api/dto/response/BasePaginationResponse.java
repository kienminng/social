package com.social.api.dto.response;

import java.util.List;


public class BasePaginationResponse<T> {
    public int PageSize ;
    public int PageNo ;
    public int TotalPage;
    public long TotalItems;
    public List<T> Data ;

    public BasePaginationResponse(int pageNo, int pageSize, long totalItems, List<T> data)
    {
        PageNo = pageNo;
        PageSize = pageSize;
        TotalItems = totalItems;
        Data = data;
        TotalPage = (int)Math.ceil((double)totalItems / pageSize);
    }
}
