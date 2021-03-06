package com.benzene.platform.response;

import com.benzene.platform.entity.Branch;
import com.benzene.platform.enums.BranchViewType;
import com.benzene.util.response.SequencedResponse;

public class BranchResponse extends SequencedResponse{

	private BranchViewType viewType;
	
	public BranchResponse() {
	}

	public BranchResponse(Branch branch) {
		super(branch);
		this.viewType = branch.getViewType();
	}

	public BranchViewType getViewType() {
		return viewType;
	}
	public void setViewType(BranchViewType viewType) {
		this.viewType = viewType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BranchResponse [").append(super.toString()).append(", viewType=").append(viewType)
				.append("]");
		return builder.toString();
	}
}
