package org.hisp.dhis.gis.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.LegendSet;
import org.hisp.dhis.gis.action.export.FeatureStructure;
import org.hisp.dhis.indicator.Indicator;

public class BagSession
{
    private Indicator indicator;

    private String startDate;

    private String endDate;

    private List<org.hisp.dhis.gis.ext.Feature> features;

    private LegendSet legendSet;

    public Indicator getIndicator()
    {
        return indicator;
    }

    public void setIndicator( Indicator indicator )
    {
        this.indicator = indicator;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    public List<org.hisp.dhis.gis.ext.Feature> getFeatures()
    {
        return features;
    }

    public void setFeatures( List<org.hisp.dhis.gis.ext.Feature> features )
    {
        this.features = features;
    }

    public LegendSet getLegendSet()
    {
        return legendSet;
    }

    public void setLegendSet( LegendSet legendSet )
    {
        this.legendSet = legendSet;
    }

    public BagSession( Indicator indicator, String startDate, String endDate, List<Feature> features,
        LegendSet legendSet )
    {
        super();
        this.indicator = indicator;
        this.startDate = startDate;
        this.endDate = endDate;
        this.features = features;
        this.legendSet = legendSet;
    }

    public Title getTitle()
    {
        return new Title( this.getIndicator().getName(), this.getStartDate(), this.getEndDate() );
    }

    public List<Legend> getLegends()
    {
        for(Legend legend:this.getLegendSet().getLegends()){
            
            legend.setColor( "#" + legend.getColor() );
            
        }
        return this.getLegendSet().getLegends();
    }

    public List<FeatureStructure> getFeatureStructure()
    {
        List<FeatureStructure> featureStructure = new ArrayList<FeatureStructure>();
        for ( Feature feature : this.getFeatures() )
        {
            if(feature.getFeature()!=null){
                featureStructure.add( new FeatureStructure( feature.getFeature().getFeatureCode(), feature.getColor(),
                    feature.getAggregatedDataValue(), feature.getFeature().getOrganisationUnit().getId() ) );
                }
        }
        return featureStructure;
    }

    public Map<String, String> getFeature()
    {
        Map<String, String> result = new HashMap<String, String>();

        for ( Feature feature : this.features )
        {

            if ( feature.getFeature() != null)
            {
                String key = feature.getFeature().getFeatureCode();

                String color = feature.getColor();
                
                double value = feature.getAggregatedDataValue();

                result.put( key, color + "-" + value );
            }

        }

        return result;

    }

}
