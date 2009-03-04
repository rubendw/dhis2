package org.hisp.dhis.dataset.comparator;

import java.util.Comparator;

import org.hisp.dhis.dataset.Section;

public class SectionOrderComparator implements Comparator<Section>{

	public int compare(Section o1, Section o2) {
		// TODO Auto-generated method stub
		return o1.getSortOrder()- o2.getSortOrder();
	}

}
