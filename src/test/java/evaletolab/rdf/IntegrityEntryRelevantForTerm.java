package evaletolab.rdf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;

import evaletolab.config.WebConfig;
/**
 * Use case for intigrity queries
 * - count Entries 
 * - Brain (TS-0095) relevant For 18202 entries (NX_P61604,NX_Q15029,NX_Q07973)
 * - Liver (TS-0564) relevant For 17848 entries (NX_P61604,NX_Q15029,NX_Q07973)
 * - :classifiedWith Transport (KW-0813) is relevant for 1859 entries (NX_Q9H0U6, NX_P08195, NX_P46098)
 * - 
 *  
 * @author evaleto
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class IntegrityEntryRelevantForTerm extends TripleStore{

	@Autowired
	private Properties config;
	
	@Before
	public void setup() throws Exception {
		//
		// open session in triplestore
		open();
	}
	
	
	/**
	 * SL-0173 > Mitochondrion
	 */
	
	@Test
	public void countMitochondrionSL(){
		String q="SELECT (count(distinct  ?entry) as ?c)  WHERE  {\n" + 
				 "  ?entry :isoform/:localisation/:in/:childOf term:SL-0173  \n" + 
				 "}";
				
		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        
        assertThat("countMitochondrionSL (1'315)",1315.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));

	}	
	
	/**
	 * term:GO_0005739 > Mitochondrion term:KW-0496 
	 */
	
	@Test
	public void countMitochondrionGo(){
		String q="SELECT (count(distinct  ?entry) as ?c)  WHERE  {\n" + 
				 "  ?entry :isoform/:localisation/:in/:childOf term:GO_0005739\n" + 
				 "}";
				
		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        
        assertThat("countMitochondrionGo (1'526)",1526.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));
	}	
	
 
	
	/**
	 * unionof term:KW-0496 > Mitochondrion  
	 */
	
	@Test
	public void countMitochondrionKW(){
		String q="SELECT (count(distinct  ?entry) as ?c)  WHERE  {\n" + 
				 "  ?entry :isoform/:localisation/:in/:childOf term:KW-0496\n" + 
				 "}";
				
		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        
        assertThat("countMitochondrionKw (1'546)",1546.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));
	}	 

	/**
	 * term:KW-0496, term:GO_0005739 > Mitochondrion  
	 */
	
	@Test
	public void countMitochondrionKWorGOorSL(){
		String q="SELECT (count(distinct  ?entry) as ?c)  WHERE  {\n" + 
				"  {?entry :isoform/:localisation/:in/:childOf* term:SL-0173}\n" + 
				"  UNION\n" + 
				"  {?entry :isoform/:localisation/:in/:childOf* term:GO_0005739}\n" + 
				"}";
				
		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        
        assertThat("countMitochondrionKw (1'765)",1765.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));
	}	
		

	/**
	 * - Brain (TS-0095) relevant For 18202 entries 
	 *   NX_P61604,NX_Q15029,NX_Q07973
	 */
	@Test
	public void countProteinsRelevantForBrain_TS_0095(){
		String q="SELECT (count(distinct ?entry)as ?c) WHERE {\n" + 
				 "  ?entry :isoform/:annotation/:in/:childOf  term:TS-0564.\n" + 
				 "}";	
		
		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        assertThat("countProteinsRelevantForBrain_TS_0095 (18202)",18202.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));
	}	
	
	
	/**
	 * - Liver (TS-0564) relevant For 17848 entries 
	 *   NX_P61604,NX_Q15029,NX_Q07973
	 */
	@Test
	public void countProteinsRelevantForLiver_TS_0564(){
		String q="SELECT (count(distinct ?entry)as ?c) WHERE {\n" + 
				 "  ?entry :isoform/:annotation/:in/:childOf  term:TS-0564.\n" + 
				 "}";	

		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        assertThat("countProteinsRelevantForLiver_TS_0564 (18'894)",18894.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));
	}	
	


	
	/**
	 * proteins :classifiedWith Transport (KW-0813) is relevant for 1859 entries
	 * NX_Q9H0U6, NX_P08195, NX_P46098
	 */
	//@Test
	public void countProteinsRelevantForKW_0813(){
		String q="SELECT (count(distinct ?entry)as ?c) WHERE {\n" + 
				 "  ?entry :classifiedWith  term:KW-0813.\n" + 
				 "}";
		
		QueryExecution qe = createQueryExecution(q);
        ResultSet rs=qe.execSelect();
        assertThat("countProteinsRelevantForKW_0813 (1'868)",1868.0,closeTo(rs.next().get("c").asLiteral().getInt(),10));        
	}	

}
