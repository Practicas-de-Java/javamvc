package org.primefaces.showcase.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

public class JasperReportGeneric {

  private static final String MIME_DOC = "application/msword";
 
  public void generaInforme( HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameters, String nombreEvento) {
      
      String plantillaStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
      		"<!-- Created with Jaspersoft Studio version 6.11.0.final using JasperReports Library version 6.11.0-0c4056ccaa4d25a5a8c45672d2f764ea3498bebb  -->\r\n" + 
      		"<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"Blank_A4\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"0c7f9ffd-8811-4e2a-a118-5c53d3ac10d0\">\r\n" + 
      		"	<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"One Empty Record\"/>\r\n" + 
      		"	<parameter name=\"TITULO\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"CONVOCANTES\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"PONENTES\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"PRESIDE\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"MODERA\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"TIPOEVENTO\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"SALA\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"FECHA\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"RESUMEN\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"CONTACTO\" class=\"java.lang.String\"/>\r\n" + 
      		"	<parameter name=\"HORA\" class=\"java.lang.String\"/>\r\n" + 
      		"	<queryString>\r\n" + 
      		"		<![CDATA[]]>\r\n" + 
      		"	</queryString>\r\n" + 
      		"	<background>\r\n" + 
      		"		<band splitType=\"Stretch\"/>\r\n" + 
      		"	</background>\r\n" + 
      		"	<title>\r\n" + 
      		"		<band height=\"30\" splitType=\"Stretch\">\r\n" + 
      		"			<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"231\" y=\"0\" width=\"100\" height=\"30\" uuid=\"52cc879f-8d8b-4c25-b714-50b88c76f930\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{TITULO}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"		</band>\r\n" + 
      		"	</title>\r\n" + 
      		"	<detail>\r\n" + 
      		"		<band height=\"802\" splitType=\"Stretch\">\r\n" + 
      		"			<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.FreeLayout\"/>\r\n" + 
      		"			<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"			<break>\r\n" + 
      		"				<reportElement x=\"0\" y=\"0\" width=\"555\" height=\"1\" uuid=\"e5b8c77a-ec5f-4655-800c-d817bc86b732\"/>\r\n" + 
      		"			</break>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"13\" width=\"91\" height=\"30\" uuid=\"2a2fdef1-f220-4a82-b069-680faa5fb102\"/>\r\n" + 
      		"				<text><![CDATA[CONVOCANTES]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"13\" width=\"443\" height=\"67\" uuid=\"ada23676-2743-419b-a921-e6b6cd193f21\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{CONVOCANTES}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"89\" width=\"91\" height=\"30\" uuid=\"1649a3ac-9b71-4ae7-8d93-1aefeaa58244\"/>\r\n" + 
      		"				<text><![CDATA[PONENTES]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"89\" width=\"442\" height=\"69\" uuid=\"5150c6ee-b02a-491c-8c53-09f7b4c2510e\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{PONENTES}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"169\" width=\"91\" height=\"30\" uuid=\"b0ee463e-1b18-4376-af92-9a906c259de0\"/>\r\n" + 
      		"				<text><![CDATA[PRESIDE]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"170\" width=\"442\" height=\"30\" uuid=\"3236d0da-7cec-4aff-8ed8-afcf55b97a29\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{PRESIDE}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"213\" width=\"91\" height=\"30\" uuid=\"a8320b6d-328e-415f-8ea3-b105e75353cc\"/>\r\n" + 
      		"				<text><![CDATA[MODERA]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"215\" width=\"440\" height=\"30\" uuid=\"c40f302d-f883-47a3-8933-7461faf41cf0\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{MODERA}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"258\" width=\"91\" height=\"30\" uuid=\"e95dc4a7-da92-48ec-a28e-5b9bbdc52d5b\"/>\r\n" + 
      		"				<text><![CDATA[TIPO]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"258\" width=\"439\" height=\"30\" uuid=\"fa0cdc8e-ba96-4e7b-a468-3d8f94f48ce0\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{TIPOEVENTO}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"304\" width=\"91\" height=\"30\" uuid=\"d2860b1e-3d15-4a17-b1cf-7d9b9c7d716c\"/>\r\n" + 
      		"				<text><![CDATA[SALA]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"305\" width=\"442\" height=\"30\" uuid=\"3b9e6a1f-14dc-4ac2-aee5-d3d1a31bc764\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{SALA}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"346\" width=\"91\" height=\"30\" uuid=\"d59eec67-b48a-4b32-933f-fc4b5eae5e1d\"/>\r\n" + 
      		"				<text><![CDATA[FECHA]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"346\" width=\"441\" height=\"30\" uuid=\"2b90bfaa-ff54-44fb-a8a0-0aa20545ded0\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{FECHA}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"431\" width=\"91\" height=\"30\" uuid=\"30be1dd7-e99a-43ae-97d1-79f44d0c23a6\"/>\r\n" + 
      		"				<text><![CDATA[RESUMEN]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"427\" width=\"442\" height=\"228\" uuid=\"5030ca6c-850d-4e2c-8618-c386ce284392\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{RESUMEN}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"667\" width=\"91\" height=\"30\" uuid=\"cd268e63-8bb7-4e5e-b4ed-b3405e33bf67\"/>\r\n" + 
      		"				<text><![CDATA[CONTACTO]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"664\" width=\"443\" height=\"84\" uuid=\"b7674462-6239-4974-bc81-1b2550d9c14d\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{CONTACTO}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<staticText>\r\n" + 
      		"				<reportElement x=\"0\" y=\"390\" width=\"91\" height=\"30\" uuid=\"03344b9a-687f-48a6-aa46-2f741a1fd08e\"/>\r\n" + 
      		"				<text><![CDATA[HORA]]></text>\r\n" + 
      		"			</staticText>\r\n" + 
      		"			<textField>\r\n" + 
      		"				<reportElement x=\"111\" y=\"390\" width=\"439\" height=\"30\" uuid=\"a536b1d6-a568-4c88-84d3-df854df034d1\"/>\r\n" + 
      		"				<textFieldExpression><![CDATA[$P{HORA}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"		</band>\r\n" + 
      		"	</detail>\r\n" + 
      		"</jasperReport>\r\n" + 
      		"";
      
      // detecta numericamente el codigo del formato
      String extension = ".docx";

      // se obtiene la plantilla compilada y se guarda en un objeto report
      JasperReport report;
	try {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(plantillaStr.getBytes());;
		report = JasperCompileManager.compileReport(inputStream );
	      //
	      // se rellena el informe con los datos
	      JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

	      // exportacion al formato deseado
	      JRDocxExporter exporter = new JRDocxExporter();
	      // asignacion de parametros para el exportador
	      SimpleExporterInput sEI = new SimpleExporterInput(jasperPrint);
	      exporter.setExporterInput(sEI);
	      // se abre el output de la respuesta y se escribe en el mismo report formateado
	      try {
	          // fija el tipo MIME de la respuesta
		      response.addHeader("Content-Disposition", "filename=" + nombreEvento + extension);
	          response.setContentType(MIME_DOC);
	          response.setBufferSize(2000000);
	    	  OutputStream oS = response.getOutputStream();
	    	  OutputStreamExporterOutput oSEO = new SimpleOutputStreamExporterOutput(oS);
	    	  exporter.setExporterOutput(oSEO);
		      exporter.exportReport();
		      FacesContext.getCurrentInstance().responseComplete();
		      return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (JRException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void generaBoletin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameters, String boletin, List<ElementoBoletin> boletos) {
      String plantillaStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
      		"<!-- Created with Jaspersoft Studio version 6.11.0.final using JasperReports Library version 6.11.0-0c4056ccaa4d25a5a8c45672d2f764ea3498bebb  -->\r\n" + 
      		"<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"Boleto\" pageWidth=\"612\" pageHeight=\"792\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"ff49ea3c-c8f9-41c2-ab5d-565ca533ff2c\">\r\n" + 
      		"	<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"Beans Collection\"/>\r\n" + 
      		"	<subDataset name=\"Dataset1\" whenResourceMissingType=\"Empty\" uuid=\"4e0eea44-5343-4299-8ddd-3171297a5c09\">\r\n" + 
      		"		<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"Beans Collection\"/>\r\n" + 
      		"		<queryString>\r\n" + 
      		"			<![CDATA[]]>\r\n" + 
      		"		</queryString>\r\n" + 
      		"		<field name=\"fecha\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"secciones\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"titulo\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"preside\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"modera\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"presentador\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"ponentes\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"intervinientes\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"sala\" class=\"java.lang.String\"/>\r\n" + 
      		"		<field name=\"hora\" class=\"java.lang.String\"/>\r\n" + 
      		"	</subDataset>\r\n" + 
      		"	<queryString>\r\n" + 
      		"		<![CDATA[]]>\r\n" + 
      		"	</queryString>\r\n" + 
      		"	<field name=\"fecha\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"secciones\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"titulo\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"preside\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"modera\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"presentador\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"ponentes\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"intervinientes\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"sala\" class=\"java.lang.String\"/>\r\n" + 
      		"	<field name=\"hora\" class=\"java.lang.String\"/>\r\n" + 
      		"	<background>\r\n" + 
      		"		<band splitType=\"Stretch\"/>\r\n" + 
      		"	</background>\r\n" + 
      		"	<detail>\r\n" + 
      		"		<band height=\"65\" splitType=\"Stretch\">\r\n" + 
      		"			<componentElement>\r\n" + 
      		"				<reportElement  positionType=\"Float\" isPrintRepeatedValues=\"false\" mode=\"Opaque\" x=\"0\" y=\"0\" width=\"569\" height=\"65\" uuid=\"ecbfa30d-2d27-40b6-8d15-04bb9761ae33\">\r\n" + 
      		"					<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.FreeLayout\"/>\r\n" + 
      		"				</reportElement>\r\n" + 
      		"				<jr:list xmlns:jr=\"http://jasperreports.sourceforge.net/jasperreports/components\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd\" printOrder=\"Vertical\">\r\n" + 
      		"					<datasetRun subDataset=\"Dataset1\" uuid=\"b92b0e05-dd7d-42e8-ba44-756a8142a5b4\">\r\n" + 
      		"						<dataSourceExpression><![CDATA[new net.sf.jasperreports.engine.JREmptyDataSource()]]></dataSourceExpression>\r\n" + 
      		"					</datasetRun>\r\n" + 
      		"					<jr:listContents height=\"65\" width=\"569\"/>\r\n" + 
      		"				</jr:list>\r\n" + 
      		"			</componentElement>\r\n" + 
      		"			<textField isStretchWithOverflow=\"true\" textAdjust=\"ScaleFont\">\r\n" + 
      		"				<reportElement  stretchType=\"RelativeToTallestObject\" isPrintRepeatedValues=\"false\" mode=\"Opaque\" x=\"3\" y=\"1\" width=\"564\" height=\"9\"  uuid=\"0a732a75-d1e3-4a40-98d1-95348a5dd3eb\">\r\n" + 
      		"					<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.consume.space.on.overflow\" value=\"true\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.print.keep.full.text\" value=\"true\"/>\r\n" + 
      		"				</reportElement>\r\n" + 
      		"				<textElement>\r\n" + 
      		"					<font fontName=\"Cantarell Extra Bold\" size=\"9\"/>\r\n" + 
      		"				</textElement>\r\n" + 
      		"				<textFieldExpression><![CDATA[$F{fecha}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<textField isStretchWithOverflow=\"true\" textAdjust=\"ScaleFont\" evaluationTime=\"Band\">\r\n" + 
      		"				<reportElement  positionType=\"Float\" stretchType=\"RelativeToTallestObject\" isPrintRepeatedValues=\"false\" mode=\"Opaque\" x=\"3\" y=\"10\" width=\"564\" height=\"11\"  uuid=\"5a18a40d-802a-4c44-a363-6f6a4d453172\">\r\n" + 
      		"					<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"					<property name=\"com.jaspersoft.layout.grid.weight.fixed\" value=\"true\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.consume.space.on.overflow\" value=\"true\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.print.keep.full.text\" value=\"true\"/>\r\n" + 
      		"				</reportElement>\r\n" + 
      		"				<textElement textAlignment=\"Left\" verticalAlignment=\"Top\">\r\n" + 
      		"					<font fontName=\"Serif\" size=\"9\"/>\r\n" + 
      		"				</textElement>\r\n" + 
      		"				<textFieldExpression><![CDATA[$F{secciones}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<textField isStretchWithOverflow=\"true\">\r\n" + 
      		"				<reportElement positionType=\"Float\" stretchType=\"RelativeToTallestObject\" isPrintRepeatedValues=\"false\" mode=\"Opaque\" x=\"3\" y=\"21\" width=\"564\" height=\"10\" uuid=\"6b20ffd9-e261-4840-a162-385fb97e2bca\">\r\n" + 
      		"					<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.consume.space.on.overflow\" value=\"true\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.print.keep.full.text\" value=\"true\"/>\r\n" + 
      		"				</reportElement>\r\n" + 
      		"				<textElement>\r\n" + 
      		"					<font fontName=\"Lato Black\" size=\"9\"/>\r\n" + 
      		"				</textElement>\r\n" + 
      		"				<textFieldExpression><![CDATA[$F{titulo}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<textField isStretchWithOverflow=\"true\">\r\n" + 
      		"				<reportElement  positionType=\"Float\" stretchType=\"RelativeToTallestObject\" isPrintRepeatedValues=\"false\" mode=\"Opaque\" x=\"1\" y=\"32\" width=\"567\" height=\"23\" uuid=\"fa7c79c8-be71-4ee1-afb8-0cf74458480e\">\r\n" + 
      		"					<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.consume.space.on.overflow\" value=\"true\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.print.keep.full.text\" value=\"true\"/>\r\n" + 
      		"				</reportElement>\r\n" + 
      		"				<textElement>\r\n" + 
      		"					<font fontName=\"Serif\" size=\"9\"/>\r\n" + 
      		"				</textElement>\r\n" + 
      		"				<textFieldExpression><![CDATA[$F{preside} + $F{presentador} + $F{modera} +$F{ponentes} + $F{intervinientes}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"			<textField isStretchWithOverflow=\"true\">\r\n" + 
      		"				<reportElement  positionType=\"Float\" stretchType=\"RelativeToTallestObject\" isPrintRepeatedValues=\"false\" mode=\"Opaque\" x=\"3\" y=\"55\" width=\"565\" height=\"9\" uuid=\"830adb87-1a93-4670-ac42-08001db414dc\">\r\n" + 
      		"					<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.consume.space.on.overflow\" value=\"true\"/>\r\n" + 
      		"					<property name=\"net.sf.jasperreports.print.keep.full.text\" value=\"true\"/>\r\n" + 
      		"				</reportElement>\r\n" + 
      		"				<textElement>\r\n" + 
      		"					<font fontName=\"Serif\" size=\"8\"/>\r\n" + 
      		"				</textElement>\r\n" + 
      		"				<textFieldExpression><![CDATA[$F{sala}+$F{hora}]]></textFieldExpression>\r\n" + 
      		"			</textField>\r\n" + 
      		"		</band>\r\n" + 
      		"	</detail>\r\n" + 
      		"</jasperReport>\r\n" + 
      		"";
        
        // detecta numericamente el codigo del formato
        String extension = ".docx";

        // se obtiene la plantilla compilada y se guarda en un objeto report
        JasperReport report;
  	try {
  		ByteArrayInputStream inputStream = new ByteArrayInputStream(plantillaStr.getBytes());;
  		report = JasperCompileManager.compileReport(inputStream );
  	      //
  	      // se rellena el informe con los datos
  	      JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource((Collection<ElementoBoletin>) parameters.get("Boletos"), true));

  	      // exportacion al formato deseado
  	      JRDocxExporter exporter = new JRDocxExporter();
  	      // asignacion de parametros para el exportador
  	      SimpleExporterInput sEI = new SimpleExporterInput(jasperPrint);
  	      exporter.setExporterInput(sEI);
  	      // se abre el output de la respuesta y se escribe en el mismo report formateado
  	      try {
  	          // fija el tipo MIME de la respuesta
  		      response.addHeader("Content-Disposition", "filename=" + boletin + extension);
  	          response.setContentType(MIME_DOC);
  	          response.setBufferSize(2000000);
  	    	  OutputStream oS = response.getOutputStream();
  	    	  OutputStreamExporterOutput oSEO = new SimpleOutputStreamExporterOutput(oS);
  	    	  exporter.setExporterOutput(oSEO);
  		      exporter.exportReport();
  		      FacesContext.getCurrentInstance().responseComplete();
  		      return;
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	} catch (JRException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
  }
}
