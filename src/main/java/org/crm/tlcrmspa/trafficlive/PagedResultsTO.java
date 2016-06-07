package org.crm.tlcrmspa.trafficlive;

import java.util.List;

public class PagedResultsTO<TO extends BaseTO> {

	private List<TO> resultList;
    private int maxResults;

    public Boolean hasMorePages() {
    	return resultList.size() > 0;
    }
    
	public List<TO> getResultList() {
		return resultList;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setResultList(List<TO> resultList) {
		this.resultList = resultList;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
