package com.library.app.common.model.filter;

public class GenericFilter {
	private PaginationData paginationData;

	public GenericFilter(PaginationData paginationData) {
		this.paginationData = paginationData;
	}

	public GenericFilter() {
	}
	
	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}
	
	public PaginationData getPaginationData() {
		return paginationData;
	}
	
	
	public boolean hasPaginationData() {
		return getPaginationData() !=null;
	}
	
	public boolean hasOrderField() {
		return hasPaginationData() && getPaginationData().getOrderField() != null;
	}

}
