/** **********************************************************************
 *
 * Copyright 2018 Jochen Staerk
 *
 * Use is subject to license terms.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *********************************************************************** */
package org.mustangproject.ZUGFeRD;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MustangReaderWriterTest extends MustangReaderTestCase {

	@Override
	public Date getDeliveryDate() {
		return new GregorianCalendar(2019, Calendar.JUNE, 10).getTime();
	}

	@Override
	public Date getDueDate() {
		return new GregorianCalendar(2019, Calendar.JULY, 1).getTime();
	}

	@Override
	public Date getIssueDate() {
		return new GregorianCalendar(2019, Calendar.JUNE, 10).getTime();
	}

	@Override
	public String getNumber() {
		return "RE-20190610/507";
	}

	@Override
	public String getOwnCountry() {
		return "DE";
	}

	@Override
	public String getOwnLocation() {
		return "Stadthausen";
	}

	@Override
	public String getOwnOrganisationName() {
		return "Bei Spiel GmbH";
	}

	@Override
	public String getOwnStreet() {
		return "Ecke 12";
	}

	@Override
	public String getOwnTaxID() {
		return "22/815/0815/4";
	}

	@Override
	public String getOwnVATID() {
		return "DE136695976";
	}

	@Override
	public String getOwnZIP() {
		return "12345";
	}

	@Override
	public IZUGFeRDExportableContact getRecipient() {
		return new RecipientContact();
	}
	
	@Override
	public IZUGFeRDExportableContact getOwnContact() {
		return new SenderContact();
	}

	@Override
	public String getOwnOrganisationFullPlaintextInfo() {
		return null;
	}

	@Override
	public String getCurrency() {
		return "EUR";
	}

	@Override
	public IZUGFeRDExportableItem[] getZFItems() {
		Item[] allItems = new Item[3];
		Product designProduct = new Product("", "Design (hours): Of a sample invoice", "HUR",
				new BigDecimal("7.000000"));
		Product balloonProduct = new Product("", "Ballons: various colors, ~2000ml", "C62", new BigDecimal("19.000000"));
		Product airProduct = new Product("", "Hot air „heiße Luft“ (litres)", "LTR", new BigDecimal("19.000000"));

		allItems[0] = new Item(new BigDecimal("160"), new BigDecimal("1"), designProduct);
		allItems[1] = new Item(new BigDecimal("0.79"), new BigDecimal("400"), balloonProduct);
		allItems[2] = new Item(new BigDecimal("0.025"), new BigDecimal("800"), airProduct);
		return allItems;
	}

	@Override
	public String getPaymentTermDescription() {
		SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return "Remit until " + isoDateFormat.format(getDueDate());
	}

	@Override
	public IZUGFeRDAllowanceCharge[] getZFAllowances() {
		return null;
		// throw new UnsupportedOperationException("Not supported yet."); //To change
		// body of generated methods, choose Tools | Templates.
	}

	@Override
	public IZUGFeRDAllowanceCharge[] getZFCharges() {
		return null;
		// throw new UnsupportedOperationException("Not supported yet."); //To change
		// body of generated methods, choose Tools | Templates.
	}

	@Override
	public IZUGFeRDAllowanceCharge[] getZFLogisticsServiceCharges() {
		return null;
		// throw new UnsupportedOperationException("Not supported yet."); //To change
		// body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getReferenceNumber() {
		return "AB321";
	}

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public MustangReaderWriterTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MustangReaderWriterTest.class);
	}

	// //////// TESTS
	// //////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * The importer test imports from
	 * ./src/test/MustangGnuaccountingBeispielRE-20170509_505.pdf to check the
	 * values. --> as only Name Ascending is supported for Test Unit sequence, I
	 * renamed the this test-A-Export to run before testZExport
	 *
	 * @throws IOException
	 */

	public void testAImport() throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream("/MustangGnuaccountingBeispielRE-20170509_505.pdf");
		ZUGFeRDImporter zi = new ZUGFeRDImporter(inputStream);

		// Reading ZUGFeRD
		assertEquals(zi.getAmount(), "571.04");
		assertEquals(zi.getBLZ(), getTradeSettlementPayment()[0].getOwnBLZ());
		assertEquals(zi.getBIC(), getTradeSettlementPayment()[0].getOwnBIC());
		assertEquals(zi.getIBAN(),getTradeSettlementPayment()[0].getOwnIBAN());
		assertEquals(zi.getKTO(), getTradeSettlementPayment()[0].getOwnKto());
		assertEquals(zi.getHolder(), getOwnOrganisationName());
		assertEquals(zi.getForeignReference(), "RE-20170509/505");
	}

	public void testForeignImport() throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream("/zugferd_invoice.pdf");
		ZUGFeRDImporter zi = new ZUGFeRDImporter(inputStream);

		// Reading ZUGFeRD
		String amount = zi.getAmount();

		assertEquals("<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n" +
				"    <x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n" +
				"      <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:xmpMM=\"http://ns.adobe.com/xap/1.0/mm/\">\n" +
				"          <xmpMM:InstanceID>uuid:6776DD95-DAF6-768E-DAE6-2697750D95C5</xmpMM:InstanceID>\n" +
				"          <xmpMM:DocumentID>uuid:47380404-5938-FE2C-8F0B-B505DCE43BF5</xmpMM:DocumentID>\n" +
				"        </rdf:Description>\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:zf=\"urn:ferd:pdfa:CrossIndustryDocument:invoice:1p0#\">\n" +
				"          <zf:ConformanceLevel>BASIC</zf:ConformanceLevel>\n" +
				"          <zf:DocumentFileName>ZUGFeRD-invoice.xml</zf:DocumentFileName>\n" +
				"          <zf:DocumentType>INVOICE</zf:DocumentType>\n" +
				"          <zf:Version>1.0</zf:Version>\n" +
				"        </rdf:Description>\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:pdfaExtension=\"http://www.aiim.org/pdfa/ns/extension/\" xmlns:pdfaSchema=\"http://www.aiim.org/pdfa/ns/schema#\" xmlns:pdfaProperty=\"http://www.aiim.org/pdfa/ns/property#\">\n" +
				"          <pdfaExtension:schemas>\n" +
				"            <rdf:Bag>\n" +
				"              <rdf:li rdf:parseType=\"Resource\">\n" +
				"                <pdfaSchema:schema>ZUGFeRD PDFA Extension Schema</pdfaSchema:schema>\n" +
				"                <pdfaSchema:namespaceURI>urn:ferd:pdfa:CrossIndustryDocument:invoice:1p0#</pdfaSchema:namespaceURI>\n" +
				"                <pdfaSchema:prefix>zf</pdfaSchema:prefix>\n" +
				"                <pdfaSchema:property>\n" +
				"                  <rdf:Seq>\n" +
				"                    <rdf:li rdf:parseType=\"Resource\">\n" +
				"                      <pdfaProperty:name>DocumentFileName</pdfaProperty:name>\n" +
				"                      <pdfaProperty:valueType>Text</pdfaProperty:valueType>\n" +
				"                      <pdfaProperty:category>external</pdfaProperty:category>\n" +
				"                      <pdfaProperty:description>name of the embedded XML invoice file</pdfaProperty:description>\n" +
				"                    </rdf:li>\n" +
				"                    <rdf:li rdf:parseType=\"Resource\">\n" +
				"                      <pdfaProperty:name>DocumentType</pdfaProperty:name>\n" +
				"                      <pdfaProperty:valueType>Text</pdfaProperty:valueType>\n" +
				"                      <pdfaProperty:category>external</pdfaProperty:category>\n" +
				"                      <pdfaProperty:description>INVOICE</pdfaProperty:description>\n" +
				"                    </rdf:li>\n" +
				"                    <rdf:li rdf:parseType=\"Resource\">\n" +
				"                      <pdfaProperty:name>Version</pdfaProperty:name>\n" +
				"                      <pdfaProperty:valueType>Text</pdfaProperty:valueType>\n" +
				"                      <pdfaProperty:category>external</pdfaProperty:category>\n" +
				"                      <pdfaProperty:description>The actual version of the ZUGFeRD XML schema</pdfaProperty:description>\n" +
				"                    </rdf:li>\n" +
				"                    <rdf:li rdf:parseType=\"Resource\">\n" +
				"                      <pdfaProperty:name>ConformanceLevel</pdfaProperty:name>\n" +
				"                      <pdfaProperty:valueType>Text</pdfaProperty:valueType>\n" +
				"                      <pdfaProperty:category>external</pdfaProperty:category>\n" +
				"                      <pdfaProperty:description>The conformance level of the embedded ZUGFeRD data</pdfaProperty:description>\n" +
				"                    </rdf:li>\n" +
				"                  </rdf:Seq>\n" +
				"                </pdfaSchema:property>\n" +
				"              </rdf:li>\n" +
				"            </rdf:Bag>\n" +
				"          </pdfaExtension:schemas>\n" +
				"        </rdf:Description>\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:pdfaid=\"http://www.aiim.org/pdfa/ns/id/\">\n" +
				"          <pdfaid:part>3</pdfaid:part>\n" +
				"          <pdfaid:conformance>B</pdfaid:conformance>\n" +
				"        </rdf:Description>\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\">\n" +
				"          <xmp:CreateDate>2014-07-11T13:39:46+02:00</xmp:CreateDate>\n" +
				"          <xmp:ModifyDate>2014-07-11T13:39:46+02:00</xmp:ModifyDate>\n" +
				"          <xmp:CreatorTool>zugferd_invoice.java</xmp:CreatorTool>\n" +
				"          <xmp:MetadataDate>2014-07-11T13:39:46+02:00</xmp:MetadataDate>\n" +
				"        </rdf:Description>\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\">\n" +
				"          <pdf:Producer>PDFlib Personalization Server 9.0.3 (JDK 1.6/Mac OS X-10.6 64)</pdf:Producer>\n" +
				"        </rdf:Description>\n" +
				"        <rdf:Description rdf:about=\"\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
				"          <dc:title>\n" +
				"            <rdf:Alt>\n" +
				"              <rdf:li xml:lang=\"x-default\">ZUGFeRD Rechnung $Revision: 1.13 $</rdf:li>\n" +
				"            </rdf:Alt>\n" +
				"          </dc:title>\n" +
				"        </rdf:Description>\n" +
				"      </rdf:RDF>\n" +
				"    </x:xmpmeta>\n" +
				"<?xpacket end=\"r\"?>", zi.getXMP());
		// this resembles the data written in MustangReaderWriterCustomXMLTest
		assertEquals(amount, "1005.55");

	}


	public void testMigratePDFA1ToA3() throws IOException {
// just make sure there is no Exception
		InputStream SOURCE_PDF = this.getClass()
				.getResourceAsStream("/MustangGnuaccountingBeispielRE-20171118_506blanko.pdf");


		ZUGFeRDExporter ze = new ZUGFeRDExporterFromA1Factory().setAttachZUGFeRDHeaders(false).load(SOURCE_PDF);

		File tempFile = File.createTempFile("ZUGFeRD-", "-test");
		ze.export(tempFile.getName());
		tempFile.deleteOnExit();
	}

	/**
	 * The exporter test bases on @{code
	 * ./src/test/MustangGnuaccountingBeispielRE-20140703_502blanko.pdf}, adds
	 * metadata, writes to @{code ./target/testout-*} and then imports to check the
	 * values. It would not make sense to have it run before the less complex
	 * importer test (which is probably redundant) --> as only Name Ascending is
	 * supported for Test Unit sequence, I renamed the Exporter Test test-Z-Export
	 */
	public void testZExport() throws Exception {

		final String TARGET_PDF = "./target/testout-MustangGnuaccountingBeispielRE-20171118_506new.pdf";

		// the writing part
		try (InputStream SOURCE_PDF = this.getClass()
				.getResourceAsStream("/MustangGnuaccountingBeispielRE-20190610_507blanko.pdf");

			 ZUGFeRDExporter ze = new ZUGFeRDExporterFromA1Factory().setZUGFeRDVersion(2).setZUGFeRDConformanceLevel(ZUGFeRDConformanceLevel.EN16931).load(SOURCE_PDF)) {

			ze.PDFattachZugferdFile(this);
			ze.disableAutoClose(true);
			ze.export(TARGET_PDF);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ze.export(baos);
			ze.close();
			String pdfContent = baos.toString("UTF-8");
			assertFalse(pdfContent.indexOf("(via mustangproject.org") == -1);
			// check for pdf-a schema extension
//			assertFalse(pdfContent.indexOf("<zf:ConformanceLevel>EN 16931</zf:ConformanceLevel>") == -1);
//			assertFalse(pdfContent.indexOf("<pdfaSchema:prefix>zf</pdfaSchema:prefix>") == -1);
//			assertFalse(pdfContent.indexOf("urn:zugferd:pdfa:CrossIndustryDocument:invoice:2p0#") == -1);
			
		}

		// now check the contents (like MustangReaderTest)
		ZUGFeRDImporter zi = new ZUGFeRDImporter(TARGET_PDF);

		// Reading ZUGFeRD
		assertEquals(zi.getAmount(), "571.04");
		assertEquals(zi.getBIC(), getTradeSettlementPayment()[0].getOwnBIC());
		assertEquals(zi.getReference(), getReferenceNumber());
		assertEquals(zi.getIBAN(), getTradeSettlementPayment()[0].getOwnIBAN());
		assertEquals(zi.getKTO(), getTradeSettlementPayment()[0].getOwnKto());
		assertEquals(zi.getHolder(), getOwnOrganisationName());
		assertEquals(zi.getForeignReference(), getNumber());
	}

	
	public void testFXExport() throws Exception {

		final String TARGET_PDF = "./target/testout-MustangGnuaccountingBeispielRE-20171118_506fx.pdf";

		// the writing part
		try (InputStream SOURCE_PDF = this.getClass()
				.getResourceAsStream("/MustangGnuaccountingBeispielRE-20171118_506blanko.pdf");

			ZUGFeRDExporter ze = new ZUGFeRDExporterFromA1Factory().setZUGFeRDVersion(2).setZUGFeRDConformanceLevel(ZUGFeRDConformanceLevel.EN16931).load(SOURCE_PDF)) {
			ze.setFacturX();
			ze.PDFattachZugferdFile(this);
			ze.disableAutoClose(true);
			ze.export(TARGET_PDF);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ze.export(baos);
			ze.close();
			String pdfContent = baos.toString("UTF-8");
			assertFalse(pdfContent.indexOf("(via mustangproject.org") == -1);
			// check for pdf-a schema extension
			assertFalse(pdfContent.indexOf("<fx:ConformanceLevel>EN 16931</fx:ConformanceLevel>") == -1);
			assertFalse(pdfContent.indexOf("<pdfaSchema:prefix>fx</pdfaSchema:prefix>") == -1);
			assertFalse(pdfContent.indexOf("urn:cen.eu:en16931:2017") == -1);
		}

		// now check the contents (like MustangReaderTest)
		ZUGFeRDImporter zi = new ZUGFeRDImporter(TARGET_PDF);

		// Reading ZUGFeRD
		assertEquals(zi.getAmount(), "571.04");
		assertEquals(zi.getBIC(), getTradeSettlementPayment()[0].getOwnBIC());
		assertEquals(zi.getIBAN(), getTradeSettlementPayment()[0].getOwnIBAN());
		assertEquals(zi.getKTO(), getTradeSettlementPayment()[0].getOwnKto());
		assertEquals(zi.getHolder(), getOwnOrganisationName());
		assertEquals(zi.getForeignReference(), getNumber());
	}

	/**
	 * @throws Exception
	 * @Test(expected = IndexOutOfBoundsException.class)
	 */
	public void testExceptionOnPDF14() throws Exception {

		final String TARGET_PDF = "./target/testout-MustangGnuaccountingBeispielRE-20170509_505new.pdf";

		boolean exceptionThrown = false;
		// the writing part
		try (InputStream SOURCE_PDF = this.getClass()
				.getResourceAsStream("/MustangGnuaccountingBeispielRE-20170509_505PDF14.pdf");

			 ZUGFeRDExporter ze = new ZUGFeRDExporterFromA1Factory().load(SOURCE_PDF)) {

			ze.PDFattachZugferdFile(this);
			ze.export(TARGET_PDF);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ze.export(baos);
		} catch (IOException ex) {
			// should throw a java.io.IOException: File is not a valid PDF/A-1 input file
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);

	}

}
