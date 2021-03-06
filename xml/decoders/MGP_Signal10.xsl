<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet	version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:db="http://docbook.org/ns/docbook"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    >
<xsl:output method="xml" encoding="utf-8"/>

<!-- Copyright (C) JMRI 2002, 2005, 2007 All rights reserved -->
<!-- $Id:$ -->
<!--                                                                        -->
<!-- JMRI is free software; you can redistribute it and/or modify it under  -->
<!-- the terms of version 2 of the GNU General Public License as published  -->
<!-- by the Free Software Foundation. See the "COPYING" file for a copy     -->
<!-- of this license.                                                       -->
<!--                                                                        -->
<!-- JMRI is distributed in the hope that it will be useful, but WITHOUT    -->
<!-- ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or  -->
<!-- FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License  -->
<!-- for more details.                                                      -->

<!--for MGP Signal 10 -->
<!-- * * * * *  Variables * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Short Signals  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<xsl:template name="ShortSignalVar">
    <xsl:param name="CV1"/>
    <xsl:param name="index"/>

    <variable item="Short Signal {$index} Rule" CV="{$CV1}" mask="XXXXXVVV" default="0">
        <enumVal>
          <enumChoice choice="Not used" value="0"/>
          <enumChoice choice="Use extra GO rule 1" value="1"/>
          <enumChoice choice="Use extra GO rule 2" value="2"/>
          <enumChoice choice="Use extra GO rule 3" value="3"/>
          <enumChoice choice="Use extra GO rule 4" value="4"/>
          <enumChoice choice="Use extra GO rule 5" value="5"/>
        </enumVal>
        <label>Short Signal <xsl:value-of select="$index"/> Rule</label>
    </variable>
</xsl:template>

<xsl:template name="AllShortSignalVars">
  <xsl:param name="CV1" select="390"/>
  <xsl:param name="index" select="1"/>

  <xsl:if test="10 >= $index">
    <xsl:call-template name="ShortSignalVar">
      <xsl:with-param name="CV1" select="$CV1"/>
      <xsl:with-param name="index" select="$index"/>
    </xsl:call-template>

    <xsl:call-template name="AllShortSignalVars">
      <xsl:with-param name="CV1" select="$CV1 +1"/>
      <xsl:with-param name="index" select="$index+1"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

<!--  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- ***** ShortSignal Pane  ********************************************************************** -->
<xsl:template name="ShortSignalInPane">
  <xsl:param name="index"/>
  
		<label><text>&#160;</text></label>
		<label><text>Signal <xsl:value-of select="$index"/></text></label>
       		  <display item="Short Signal {$index} Rule">
		<label>Rule</label>
		</display>
</xsl:template>


<!-- - - - MATCH - - - -->
<xsl:template match="pane[name='ShortSignalPane']">
	<pane>
	<name>Rules Short</name>
    <column>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="1"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="3"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="4"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="5"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="6"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="7"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="8"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="9"/>
		</xsl:call-template>
		<xsl:call-template name="ShortSignalInPane">
		  <xsl:with-param name="index" select="10"/>
		</xsl:call-template>
    </column>
   </pane>
</xsl:template>

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Repeat Signals  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<xsl:template name="RepeatSignalVar">
    <xsl:param name="CV1"/>
    <xsl:param name="index"/>

    <variable item="Repeat Signal {$index} Type" CV="{$CV1}" mask="XXXXXVVV" default="0">
        <enumVal>
          <enumChoice choice="Not used" value="0"/>
          <enumChoice choice="2 light" value="1"/>
        </enumVal>
        <label>Repeat Signal <xsl:value-of select="$index"/> Type</label>
    </variable>
    <variable item="Repeat Signal {$index} Intensity bank number" CV="{$CV1}" mask="XXVVXXXX" default="0">
        <enumVal>
          <enumChoice choice="1" value="0"/>
          <enumChoice choice="2" value="1"/>
          <enumChoice choice="3" value="2"/>
        </enumVal>
        <label>Repeat Signal <xsl:value-of select="$index"/> Intensity bank number</label>
    </variable>
    <variable item="Repeat Signal {$index} First LED number" CV="{$CV1 +1}" default="0">
        <decVal max="64"/>
        <label>Repeat Signal <xsl:value-of select="$index"/> First LED number</label>
    </variable>
	<variable item="Repeat Signal {$index} Main signal address" CV="{$CV1 +2}" default="0">
        <splitVal highCV="{$CV1 +3}" upperMask="XXXXXVVV"/>
        <label>Repeat Signal <xsl:value-of select="$index"/> Main signal address</label>
    </variable>
</xsl:template>

<xsl:template name="AllRepeatSignalVars">
  <xsl:param name="CV1" select="500"/>
  <xsl:param name="index" select="1"/>

  <xsl:if test="4 >= $index">
    <xsl:call-template name="RepeatSignalVar">
      <xsl:with-param name="CV1" select="$CV1"/>
      <xsl:with-param name="index" select="$index"/>
    </xsl:call-template>

    <xsl:call-template name="AllRepeatSignalVars">
      <xsl:with-param name="CV1" select="$CV1 +4"/>
      <xsl:with-param name="index" select="$index+1"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

<!--  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- ***** RepeatSignal Pane  ********************************************************************** -->
<xsl:template name="RepeatSignalInPane">
  <xsl:param name="index"/>
  
		<label><text>&#160;</text></label>
		<label><text>Repeat Signal <xsl:value-of select="$index"/></text></label>
        	<display item="Repeat Signal {$index} Type">
			<label>Type</label>
		</display>
		<display item="Repeat Signal {$index} First LED number">
			<label>First LED number</label>
		</display>
		<display item="Repeat Signal {$index} Intensity bank number">
			<label>Intensity bank number</label>
		</display>
		<display item="Repeat Signal {$index} Main signal address">
			<label>Main signal address</label>
		</display>
</xsl:template>

<!-- - - - MATCH - - - -->
<xsl:template match="pane[name='RepeatSignalPane']">
	<pane>
	<name>Repeat Signals</name>
    <column>
		<xsl:call-template name="RepeatSignalInPane">
		  <xsl:with-param name="index" select="1"/>
		</xsl:call-template>
		<xsl:call-template name="RepeatSignalInPane">
		  <xsl:with-param name="index" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="RepeatSignalInPane">
		  <xsl:with-param name="index" select="3"/>
		</xsl:call-template>
		<xsl:call-template name="RepeatSignalInPane">
		  <xsl:with-param name="index" select="4"/>
		</xsl:call-template>
    </column>
   </pane>
</xsl:template>


<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Direction Detection  * * * * * * *  * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<xsl:template name="DirectionDetectionVar">
    <xsl:param name="CV1"/>
    <xsl:param name="index"/>

    <variable item="Direction Detection {$index} First Address" CV="{$CV1}" default="0">
        <splitVal highCV="{$CV1 +1}" upperMask="XXXXXVVV"/>
        <label>Direction Detection <xsl:value-of select="$index"/> First Address</label>
    </variable>
    <variable item="Direction Detection {$index} Delay Free Status" CV="{$CV1 +2}" default="0">
        <decVal max="255"/>
        <label>Direction Detection <xsl:value-of select="$index"/> Delay Free Status</label>
    </variable>
    <variable item="Direction Detection {$index} Address A" CV="{$CV1 + 3}" default="0">
        <splitVal highCV="{$CV1 +4}" upperMask="XXXXXVVV"/>
        <label>Direction Detection <xsl:value-of select="$index"/> Address A</label>
    </variable>
    <variable item="Direction Detection {$index} Address B" CV="{$CV1 + 5}" default="0">
        <splitVal highCV="{$CV1 +6}" upperMask="XXXXXVVV"/>
        <label>Direction Detection <xsl:value-of select="$index"/> Address B</label>
    </variable>
</xsl:template>

<xsl:template name="AllDirectionDetectionVars">
  <xsl:param name="CV1" select="550"/>
  <xsl:param name="index" select="1"/>

  <xsl:if test="4 >= $index">
    <xsl:call-template name="DirectionDetectionVar">
      <xsl:with-param name="CV1" select="$CV1"/>
      <xsl:with-param name="index" select="$index"/>
    </xsl:call-template>

    <xsl:call-template name="AllDirectionDetectionVars">
      <xsl:with-param name="CV1" select="$CV1 +7"/>
      <xsl:with-param name="index" select="$index+1"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

<!--  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- ***** DirectionDetection Pane  ********************************************************************** -->
<xsl:template name="DirectionDetectionInPane">
  <xsl:param name="index"/>
  
		<label><text>&#160;</text></label>
		<label><text>Direction Detection <xsl:value-of select="$index"/></text></label>
        <display item="Direction Detection {$index} First Address">
			<label>First Address</label>
		</display>
		<display item="Direction Detection {$index} Delay Free Status">
			<label>Delay Free Status</label>
		</display>
		<display item="Direction Detection {$index} Address A">
			<label>Address A</label>
		</display>
		<display item="Direction Detection {$index} Address B">
			<label>Address B</label>
		</display>
</xsl:template>

<!-- - - - MATCH - - - -->
<xsl:template match="pane[name='DirectionDetectionPane']">
	<pane>
	<name>Direction Detection</name>
    <column>
		<xsl:call-template name="DirectionDetectionInPane">
		  <xsl:with-param name="index" select="1"/>
		</xsl:call-template>
		<xsl:call-template name="DirectionDetectionInPane">
		  <xsl:with-param name="index" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="DirectionDetectionInPane">
		  <xsl:with-param name="index" select="3"/>
		</xsl:call-template>
		<xsl:call-template name="DirectionDetectionInPane">
		  <xsl:with-param name="index" select="4"/>
		</xsl:call-template>
    </column>
   </pane>
</xsl:template>


<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Trigger rule statements   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
 <xsl:template name="TriggerRuleStmnt">
    <xsl:param name="CV1"/>
    <xsl:param name="ruleIndex"/>
    <xsl:param name="stmntIndex"/>

    <variable item="Rule {$ruleIndex} statement {$stmntIndex} Logic" CV="{$CV1}" mask="XXXXXXXV" default="0">
        <enumVal>
          <enumChoice choice="OR" value="0"/>
          <enumChoice choice="AND" value="1"/>
        </enumVal>
    </variable>

    <variable item="Rule {$ruleIndex} statement {$stmntIndex} Status" CV="{$CV1}" mask="XXXXXXVX" default="0">
        <enumVal>
          <enumChoice choice="0/Thrown/Free" value="0"/>
          <enumChoice choice="1/Closed/Occupied" value="1"/>
        </enumVal>
    </variable>
    <variable item="Rule {$ruleIndex} statement {$stmntIndex} Type" CV="{$CV1}" mask="XXXVVVXX" default="0">
        <enumVal>
          <enumChoice choice="Not used" value="0"/>
          <enumChoice choice="Sw status" value="1"/>
          <enumChoice choice="Occ sensor" value="2"/>
          <enumChoice choice="SE" value="3"/>
        </enumVal>
    </variable>
	<variable item="Rule {$ruleIndex} statement {$stmntIndex} Address" CV="{$CV1 +1}" default="0">
        <splitVal highCV="{$CV1 +2}" upperMask="XXXXXVVV"/>
    </variable>
 </xsl:template>
 
 <xsl:template name="TriggerRuleVar">
    <xsl:param name="CV1"/>
    <xsl:param name="index"/>

    <variable item="Rule {$index} Signal Number" CV="{$CV1}" mask="XXXVVVVX" default="0">
	    <qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>2</value></qualifier>
        <enumVal>
			<enumChoice choice="Not used" value='0'/>
			<enumChoice choice="Signal 1" value='1'/>
			<enumChoice choice="Signal 2" value='2'/>
			<enumChoice choice="Signal 3" value='3'/>
			<enumChoice choice="Signal 4" value='4'/>
			<enumChoice choice="Signal 5" value='5'/>
			<enumChoice choice="Signal 6" value='6'/>
			<enumChoice choice="Signal 7" value='7'/>
			<enumChoice choice="Signal 8" value='8'/>
			<enumChoice choice="Signal 9" value='9'/>
			<enumChoice choice="Signal 10" value='10'/>
        </enumVal>
    </variable>

    <variable item="Rule {$index}, Controlled status" CV="{$CV1}" mask="XXXVXXXXX" default="0">
	    <qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>2</value></qualifier>
        <enumVal>
          <enumChoice choice="Signal Stop" value="0"/>
          <enumChoice choice="Signal Go" value="1"/>
        </enumVal>
    </variable>

	<xsl:call-template name="TriggerRuleStmnt">
	  <xsl:with-param name="CV1" select="$CV1 +1"/>
	  <xsl:with-param name="ruleIndex" select="$index"/>
	  <xsl:with-param name="stmntIndex" select="1"/>
	</xsl:call-template>
	<xsl:call-template name="TriggerRuleStmnt">
	  <xsl:with-param name="CV1" select="$CV1 +4"/>
	  <xsl:with-param name="ruleIndex" select="$index"/>
	  <xsl:with-param name="stmntIndex" select="2"/>
	</xsl:call-template>
	<xsl:call-template name="TriggerRuleStmnt">
	  <xsl:with-param name="CV1" select="$CV1 +7"/>
	  <xsl:with-param name="ruleIndex" select="$index"/>
	  <xsl:with-param name="stmntIndex" select="3"/>
	</xsl:call-template>
</xsl:template>

<xsl:template name="AllTriggerRuleVars">
	<xsl:param name="CV1" select="600"/>
	<xsl:param name="index" select="1"/>

	<xsl:if test="11 > $index">

		<xsl:call-template name="TriggerRuleVar">
			<xsl:with-param name="CV1" select="$CV1"/>
			<xsl:with-param name="index" select="$index"/>
		</xsl:call-template>

		<xsl:call-template name="AllTriggerRuleVars">
			<xsl:with-param name="CV1" select="$CV1 +10"/>
			<xsl:with-param name="index" select="$index+1"/>
		</xsl:call-template>

	</xsl:if>
</xsl:template>

<xsl:template name="TriggerRuleInPane">
	<xsl:param name="index"/>

	<label><text>&#160;</text></label>
	<label><text>Rule <xsl:value-of select="$index"/></text></label>
	<display item="Rule {$index} Signal Number">
		<label>Signal Number</label>
	</display>
	<display item="Rule {$index}, Controlled status">
		<label>Controlled status</label>
	</display>

	<label><text>&#160;</text></label>
	<display item="Rule {$index} statement 1 Logic">
		<label>Stmnt 1 Logic</label>
	</display>
	<display item="Rule {$index} statement 1 Status">
		<label>Stmnt 1 Status</label>
	</display>
	<display item="Rule {$index} statement 1 Type">
		<label>Stmnt 1 Type</label>
	</display>
	<display item="Rule {$index} statement 1 Address">
		<label>Stmnt 1 Address</label>
	</display>

	<label><text>&#160;</text></label>
	<display item="Rule {$index} statement 2 Logic">
		<label>Stmnt 2 Logic</label>
	</display>
	<display item="Rule {$index} statement 2 Status">
		<label>Stmnt 2 Status</label>
	</display>
	<display item="Rule {$index} statement 2 Type">
		<label>Stmnt 2 Type</label>
	</display>
	<display item="Rule {$index} statement 2 Address">
		<label>Stmnt 2 Address</label>
	</display>

	<label><text>&#160;</text></label>
	<display item="Rule {$index} statement 3 Logic">
		<label>Stmnt 3 Logic</label>
	</display>
	<display item="Rule {$index} statement 3 Status">
		<label>Stmnt 3 Status</label>
	</display>
	<display item="Rule {$index} statement 3 Type">
		<label>Stmnt 3 Type</label>
	</display>
	<display item="Rule {$index} statement 3 Address">
		<label>Stmnt 3 Address</label>
	</display>

</xsl:template>

<!-- - - - MATCH - - - -->

<xsl:template match="pane[name='TriggerRulePane']">
	<pane>
	<name>Trigger Rules</name>
 
	<row>
		 <column>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="1"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="2"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="3"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="4"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="5"/>
			</xsl:call-template>
		</column>
	</row>
	<row>
		 <column>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="6"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="7"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="8"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="9"/>
			</xsl:call-template>
			<xsl:call-template name="TriggerRuleInPane">
			  <xsl:with-param name="index" select="10"/>
			</xsl:call-template>
		</column>
	</row>
   </pane>
</xsl:template>

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Extra rule statements   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
 <xsl:template name="ExtraRuleVar">
    <xsl:param name="CV1"/>
    <xsl:param name="ruleIndex"/>
    <xsl:param name="goIndex"/>

    <variable item="Extra Rule {$ruleIndex} GO {$goIndex}, Logic" CV="{$CV1}" mask="XXXXXXXV" default="0">
        <enumVal>
			<enumChoice choice="OR" value='0'/>
			<enumChoice choice="AND" value='1'/>
        </enumVal>
    </variable>

    <variable item="Extra Rule {$ruleIndex} GO {$goIndex}, Type" CV="{$CV1}" mask="VVVVXXXX" default="0">
        <enumVal>
          <enumChoice choice="Not used" value="0"/>
          <enumChoice choice="Sw status" value="1"/>
          <enumChoice choice="Occ sensor" value="2"/>
          <enumChoice choice="SE" value="3"/>
          <enumChoice choice="xRule" value="5"/>
        </enumVal>
    </variable>

	<variable item="Extra Rule {$ruleIndex} GO {$goIndex}, Address" CV="{$CV1 +1}" default="0">
        <splitVal highCV="{$CV1 +2}" upperMask="XXXXVVVV"/>
    </variable>

    <variable item="Extra Rule {$ruleIndex} GO {$goIndex}, Status" CV="{$CV1 +2}" mask="XVXXXXXX" default="0">
        <enumVal>
			<enumChoice choice="Thrown/0" value='0'/>
			<enumChoice choice="CLosed/1" value='1'/>
        </enumVal>
    </variable>

</xsl:template>

<xsl:template name="AllGoExtraRuleVar">
	<xsl:param name="CV1"/>
	<xsl:param name="ruleIndex"/>
	<xsl:param name="goIndex" select="1"/>

	<xsl:if test="7 > $goIndex">

		<xsl:call-template name="ExtraRuleVar">
			<xsl:with-param name="CV1" select="$CV1"/>
			<xsl:with-param name="ruleIndex" select="$ruleIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex"/>
		</xsl:call-template>
		<xsl:call-template name="AllGoExtraRuleVar">
			<xsl:with-param name="CV1" select="$CV1 +3"/>
			<xsl:with-param name="ruleIndex" select="$ruleIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex+1"/>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<xsl:template name="AllExtraRuleVar">
	<xsl:param name="CV1" select="400"/>
	<xsl:param name="index" select="1"/>

	<xsl:if test="6 > $index">

		<xsl:call-template name="AllGoExtraRuleVar">
			<xsl:with-param name="CV1" select="$CV1"/>
			<xsl:with-param name="ruleIndex" select="$index"/>
			<xsl:with-param name="goIndex" select="1"/>
		</xsl:call-template>
		<xsl:call-template name="AllExtraRuleVar">
			<xsl:with-param name="CV1" select="$CV1 +18"/>
			<xsl:with-param name="index" select="$index +1"/>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<!-- ExtraRulesPane -->
<xsl:template name="ExtraRuleGoInPane">
	<xsl:param name="ruleIndex"/>
	<xsl:param name="goIndex"/>

	<xsl:if test="7 > $goIndex">

		<label><text>Rule <xsl:value-of select="$ruleIndex"/>, GO <xsl:value-of select="$goIndex"/></text></label>
		<display item="Extra Rule {$ruleIndex} GO {$goIndex}, Logic">
			<label>Logic</label>
		</display>
		<display item="Extra Rule {$ruleIndex} GO {$goIndex}, Type">
			<label>Type</label>
		</display>
		<display item="Extra Rule {$ruleIndex} GO {$goIndex}, Address">
			<label>Address</label>
		</display>
		<display item="Extra Rule {$ruleIndex} GO {$goIndex}, Status">
			<label>Status</label>
		</display>
		<label><text>&#160;</text></label>

		<xsl:call-template name="ExtraRuleGoInPane">
			<xsl:with-param name="ruleIndex" select="$ruleIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex +1"/>
		</xsl:call-template>
	</xsl:if>
	
</xsl:template>

<xsl:template name="AllExtraRuleInPane">
	<xsl:param name="index"/>

<!--	<xsl:if test="6 > $index"> -->
		<label><text>&#160;</text></label>
		<label><text>Rule <xsl:value-of select="$index"/></text></label>

		<xsl:call-template name="ExtraRuleGoInPane">
			<xsl:with-param name="ruleIndex" select="$index"/>
			<xsl:with-param name="goIndex" select="1"/>
		</xsl:call-template>
<!--		<xsl:call-template name="AllExtraRuleInPane">
			<xsl:with-param name="index" select="$index +1"/>
		</xsl:call-template>
		
	</xsl:if>
-->
</xsl:template>

<!-- - - - MATCH - - - -->

<xsl:template match="pane[name='ExtraRulesPane']">
	<pane>
	<name>Extra GO Rules</name>
	<column>
				<xsl:call-template name="AllExtraRuleInPane">
				  <xsl:with-param name="index" select="1"/>
				</xsl:call-template>
	</column>
	<column>
				<xsl:call-template name="AllExtraRuleInPane">
				  <xsl:with-param name="index" select="2"/>
				</xsl:call-template>
	</column>
	<column>
				<xsl:call-template name="AllExtraRuleInPane">
				  <xsl:with-param name="index" select="3"/>
				</xsl:call-template>
	</column>
	<column>
				<xsl:call-template name="AllExtraRuleInPane">
				  <xsl:with-param name="index" select="4"/>
				</xsl:call-template>
	</column>
	<column>
				<xsl:call-template name="AllExtraRuleInPane">
				  <xsl:with-param name="index" select="5"/>
				</xsl:call-template>
	</column>
   </pane>
</xsl:template>

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Signal Selectors  * * * * * * *  * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<xsl:template name="SignalSelectorsVar">
    <xsl:param name="CV1"/>
    <xsl:param name="index"/>

    <variable item="Signal Selectors {$index} Switch Address" CV="{$CV1}" default="0">
        <splitVal highCV="{$CV1 +1}" upperMask="XXXXXVVV"/>
        <label>Signal Selectors <xsl:value-of select="$index"/> Switch Address</label>
    </variable>
    <variable item="Signal Selectors {$index} Thrown Signal Address" CV="{$CV1 + 2}" default="0">
        <splitVal highCV="{$CV1 +3}" upperMask="XXXXXVVV"/>
        <label>Signal Selectors <xsl:value-of select="$index"/> Thrown Signal Address</label>
    </variable>
    <variable item="Signal Selectors {$index} Closed Signal Address" CV="{$CV1 + 4}" default="0">
        <splitVal highCV="{$CV1 +5}" upperMask="XXXXXVVV"/>
        <label>Signal Selectors <xsl:value-of select="$index"/> Closed Signal Address</label>
    </variable>
</xsl:template>

<xsl:template name="AllSignalSelectorsVars">
  <xsl:param name="CV1" select="702"/>
  <xsl:param name="index" select="1"/>

  <xsl:if test="4 >= $index">
    <xsl:call-template name="SignalSelectorsVar">
      <xsl:with-param name="CV1" select="$CV1"/>
      <xsl:with-param name="index" select="$index"/>
    </xsl:call-template>

    <xsl:call-template name="AllSignalSelectorsVars">
      <xsl:with-param name="CV1" select="$CV1 +6"/>
      <xsl:with-param name="index" select="$index+1"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

<!--  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- ***** Signal Selectors Pane  ********************************************************************** -->
<xsl:template name="SignalSelectorsInPane">
  <xsl:param name="index"/>
  
		<label><text>&#160;</text></label>
		<label><text>Signal Selectors <xsl:value-of select="$index"/></text></label>
        <display item="Signal Selectors {$index} Switch Address">
			<label>Switch Address</label>
		</display>
		<display item="Signal Selectors {$index} Thrown Signal Address">
			<label>Thrown Signal Address</label>
		</display>
		<display item="Signal Selectors {$index} Closed Signal Address">
			<label>Closed Signal Address</label>
		</display>
</xsl:template>

<!-- - - - MATCH - - - -->
<xsl:template match="pane[name='SignalSelectorsPane']">
	<pane>
	<name>Signal Selectors</name>
    <column>
		<display item="Start Address"/>
		<xsl:call-template name="SignalSelectorsInPane">
		  <xsl:with-param name="index" select="1"/>
		</xsl:call-template>
		<xsl:call-template name="SignalSelectorsInPane">
		  <xsl:with-param name="index" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="SignalSelectorsInPane">
		  <xsl:with-param name="index" select="3"/>
		</xsl:call-template>
		<xsl:call-template name="SignalSelectorsInPane">
		  <xsl:with-param name="index" select="4"/>
		</xsl:call-template>
    </column>
   </pane>
</xsl:template>



<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Signals   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<xsl:template name="SignalVars">
	<xsl:param name="CV1" select="100"/>
	<xsl:param name="sigIndex" select="1"/>

	<variable item="SignalSE {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>6</value></qualifier>
        <enumVal>
		    <enumChoice choice="Not used" value='0'/>
            <enumChoice choice="Hsi 2" value='1'/>
            <enumChoice choice="Hsi 3" value='3'/>
            <enumChoice choice="Hsi 4" value='4'/>
            <enumChoice choice="Hsi 5" value='5'/>
            <enumChoice choice="Hdvsi" value='16'/>                  <!-- note, this one is valid from decoder version 12 -->
            <enumChoice choice="Dvsi" value='7'/>
            <enumChoice choice="Fsi 2" value='8'/>
            <enumChoice choice="Fsi 3" value='9'/>
            <enumChoice choice="Repeat" value='10'/>
            <enumChoice choice="VSI mgp" value='17'/>                <!-- note, this one is valid from decoder version 13 -->
            <enumChoice choice="VSI 2" value='11'/>
            <enumChoice choice="VSI rgb" value='12'/>
            <enumChoice choice="VFSI" value='13'/>
            <enumChoice choice="VTSI" value='14'/>
            <enumChoice choice="Sl" value='15'/>                     <!-- note, this one is valid from decoder version 9 -->
            <enumChoice choice="TGOJ UT 3" value='20'/>
            <enumChoice choice="HdvM" value='6'/>
        </enumVal>
	</variable>
	<variable item="SignalDE {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>7</value></qualifier>
        <enumVal>
            <enumChoice choice="Not used" value='0'/>
            <enumChoice choice="Hp Block 2" value='1'/>
            <enumChoice choice="Hp Einf 3" value='2'/>
            <enumChoice choice="Hp Ausf 6" value='3'/>
            <enumChoice choice="V 4" value='4'/>
            <enumChoice choice="Hp/V 10" value='5'/>
            <enumChoice choice="Hp Schutz alt" value='18'/>
            <enumChoice choice="Hp Schutz neu" value='17'/>
            <enumChoice choice="Ks Vor" value='10'/>                 <!-- note, this one is valid from decoder version 9 -->
            <enumChoice choice="Ks Haupt" value='11'/>               <!-- note, this one is valid from decoder version 9 -->
            <enumChoice choice="Ks Voll" value='12'/>                <!-- note, this one is valid from decoder version 9 -->
            <enumChoice choice="Ks 'Buchstabe'" value='13'/>         <!-- note, this one is valid from decoder version 9 -->
        </enumVal>
	</variable>	
	<variable item="SignalDK {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>8</value></qualifier>
        <enumVal>
            <enumChoice choice="Not used" value='0'/>
			<enumChoice choice="I 2" value='2'/>
            <enumChoice choice="I 3" value='3'/>
            <enumChoice choice="I 4" value='4'/>
            <enumChoice choice="U 2" value='5'/>
            <enumChoice choice="U 3" value='6'/>
            <enumChoice choice="F 2" value='7'/>
            <enumChoice choice="F 3" value='8'/>
            <enumChoice choice="AM 3" value='11'/>
            <enumChoice choice="PU" value='14'/>
            <enumChoice choice="Dv" value='15'/>
            <enumChoice choice="Stop" value='10'/>                   <!-- note, this one is valid from decoder version 12 -->
        </enumVal>
	</variable>
	<variable item="SignalCZ {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>11</value></qualifier>
        <enumVal>
            <enumChoice choice="Not used" value='0'/>
            <enumChoice choice="Cz V2" value='2'/>
            <enumChoice choice="Cz V3" value='3'/>
            <enumChoice choice="Cz V4" value='4'/>
            <enumChoice choice="Cz V5" value='5'/>
            <enumChoice choice="Cz O2" value='12'/>
            <enumChoice choice="Cz O3" value='13'/>
            <enumChoice choice="Cz O3GRW" value='16'/>
            <enumChoice choice="Cz O4" value='14'/>
            <enumChoice choice="Cz P" value='6'/>
            <enumChoice choice="Cz Distant" value='10'/>
            <enumChoice choice="Cz RoadCrossing" value='20'/>        <!-- note, this one is valid from decoder version 11 -->
            <enumChoice choice="Cz StopLight" value='27'/>           <!-- note, this one is valid from decoder version 11 -->
        </enumVal>
	</variable>
	<variable item="SignalUS {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>12</value></qualifier>
        <enumVal>
            <enumChoice choice="Not used" value='0'/>
            <enumChoice choice="SigPic Us stop" value='1'/>
            <enumChoice choice="SigPic Us BN shGYR" value='2'/>
            <enumChoice choice="SigPic Us BN dhGYR" value='3'/>
            <enumChoice choice="SigPic Us BN shSL" value='4'/>
            <enumChoice choice="SigPic Us BN dhSL" value='5'/>
            <enumChoice choice="SigPic Us BN thGYR" value='6'/>
            <enumChoice choice="SigPic Us BN thSL" value='7'/>
        </enumVal>
	</variable>
	<variable item="SignalNO {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>13</value></qualifier>
        <enumVal>
            <enumChoice choice="Not used" value='0'/>
            <enumChoice choice="No Hsi2Ytter" value='2'/>
            <enumChoice choice="No Hsi2Indre" value='3'/>
            <enumChoice choice="No Hsi3Ytter" value='4'/>
            <enumChoice choice="No Hsi3Indre" value='5'/>
            <enumChoice choice="No Hsi4Ytter" value="12" minVersion='11'/>           <!-- note, this one is valid from decoder version 11 -->
            <enumChoice choice="No Hsi4Indre" value="13" minVersion='11'/>           <!-- note, this one is valid from decoder version 11 -->
            <enumChoice choice="No Hsi5Ytter" value="10" minVersion='11'/>           <!-- note, this one is valid from decoder version 11 -->
            <enumChoice choice="No Hsi5Indre" value="11" minVersion='11'/>           <!-- note, this one is valid from decoder version 11 -->
            <enumChoice choice="No Fsi2" value='6'/>
            <enumChoice choice="No Dvsi" value='7'/>
            <enumChoice choice="No Rep1" value='8'/>
            <enumChoice choice="No Rep2" value='9'/>
            <enumChoice choice="No SL" value='14' minVersion='12'/>  <!-- note, this one is valid from decoder version 12 -->
        </enumVal>
	</variable>		
	<variable item="SignalNL {$sigIndex} Type" CV="{$CV1}" mask="XXXVVVVV" default="0">
	  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>14</value></qualifier>
        <enumVal>
            <enumChoice choice="Not used" value='0'/>
            <enumChoice choice="NL 55 3" value='2'/>
            <enumChoice choice="NL 55 3 w Display" value='3'/>
            <enumChoice choice="NL 55 4" value='4'/>
            <enumChoice choice="NL 55 Dwarf" value='5'/>
        </enumVal>
	</variable>		

	<!-- <SV offset="0" type="bits" start='7' length='1' name="Default start" rw="1" minVersion='2'> -->
	<variable item="Signal {$sigIndex} Default start" CV="{$CV1}" mask="VXXXXXXX" default="1">
		<qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>2</value></qualifier>
		<enumVal>
			<enumChoice choice="Stop" value='0'/>
			<enumChoice choice="Go" value='1'/>
		</enumVal>
	</variable>

	<!-- <SV offset="1" type="int1" minValue='1'  maxValue='64' name="First LED number" rw="1"/> -->
	<variable item="Signal {$sigIndex} First LED number" CV="{$CV1+1}" default="0">
		<decVal min="1" max="64"/>
	</variable>

	<!-- <SV offset="2" type="bits" start='0' length='1' name="Short way" rw="1" minVersion='3'> -->
	<variable item="Signal {$sigIndex} Short way" CV="{$CV1+2}" mask="XXXXXXXV" default="0">
		<qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>3</value></qualifier>
		<enumVal>
			<enumChoice choice="No" value='0'/>
			<enumChoice choice="Yes" value='1'/>
		</enumVal>
	</variable>
	
	<!-- <SV offset="2" type="bits" start='1' length='3' name="Direction Control" rw="1" minVersion='1'> -->
	<variable item="Signal {$sigIndex} Direction Control" CV="{$CV1+2}" mask="XXXXVVVX" default="0">
		<enumVal>
			<enumChoice choice="Not used" value='0'/>
			<enumChoice choice="1 - West to East" value='2'/>
			<enumChoice choice="1 - East to West" value='3'/>
			<enumChoice choice="2 - West to East" value='4'/>
			<enumChoice choice="2 - East to West" value='5'/>
			<enumChoice choice="3 - West to East" value='6'/>
			<enumChoice choice="3 - East to West" value='7'/>
		</enumVal>
	</variable>

	<!-- <SV offset="2" type="bits" start='4' length='2' name="Intensity bank number" rw="1"> -->
	<variable item="Signal {$sigIndex} Intensity bank number" CV="{$CV1+2}" mask="XXVVXXXX" default="0">
		<enumVal>
			<enumChoice choice="1" value='0'/>
			<enumChoice choice="2" value='1'/>
			<enumChoice choice="3" value='2'/>
		</enumVal>
	</variable>

	<!-- <SV offset="3" type="int2" minValue='0'  maxValue='4096' name="Next signal address" rw="1"/> -->
	<variable item="Signal {$sigIndex} Next signal address" CV="{$CV1 +3}" default="0">
		<splitVal highCV="{$CV1 +4}" upperMask="XXXXVVVV"/>
	</variable>

	<!-- <SV offset="2" type="bits" start='6' length='1' name="Combine with next" rw="1" minVersion='1'> -->
	<variable item="Signal {$sigIndex} Combine with next" CV="{$CV1+2}" mask="XVXXXXXX" default="0">
		<enumVal>
			<enumChoice choice="No" value='0'/>
			<enumChoice choice="Yes" value='1'/>
		</enumVal>
	</variable>

	<!-- <SV offset="2" type="bits" start='7' length='1' name="Combined with previous" rw="0" minVersion='1'> -->
	<variable item="Signal {$sigIndex} Combined with previous" CV="{$CV1+2}" mask="VXXXXXXX" default="0">
		<enumVal>
			<enumChoice choice="No" value='0'/>
			<enumChoice choice="Yes" value='1'/>
		</enumVal>
	</variable>

	<!-- <SV offset="5" type="bits" start='0' length='12' name="Set Diverging 1, Address" minValue='0' maxValue='2048' rw="1"/> -->
	<variable item="Signal {$sigIndex} Set Diverging 1, Address" CV="{$CV1 +5}" default="0">
		<splitVal highCV="{$CV1 +6}" upperMask="XXXXXVVV"/>
	</variable>
	<!-- <SV offset="6" type="bits" start='7' length='1' name="Set Diverging 1, Use switch order" advanced="1" minVersion='3' rw="1"> -->
	<variable item="Signal {$sigIndex} Set Diverging 1, Use switch order" CV="{$CV1+6}" mask="VXXXXXXX" default="0">
		<qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>3</value></qualifier>
		<enumVal>
			<enumChoice choice="No, react on normal feedback" value='0'/>
			<enumChoice choice="Yes, handle feedback" value='1'/>
		</enumVal>
	</variable>
	
	<!-- <SV offset="7" type="bits" start='0' length='12' name="Set Diverging 2, Address" minValue='0' maxValue='2048' rw="1"/> -->
	<variable item="Signal {$sigIndex} Set Diverging 2, Address" CV="{$CV1 +7}" default="0">
		<splitVal highCV="{$CV1 +8}" upperMask="XXXXXVVV"/>
	</variable>
	<!-- <SV offset="8" type="bits" start='7' length='1' name="Set Diverging 2, Direct order" advanced="1" minVersion='3' rw="1"> -->
	<variable item="Signal {$sigIndex} Set Diverging 2, Use switch order" CV="{$CV1+8}" mask="VXXXXXXX" default="0">
		<qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>3</value></qualifier>
		<enumVal>
			<enumChoice choice="No, react on normal feedback" value='0'/>
			<enumChoice choice="Yes, handle feedback" value='1'/>
		</enumVal>
	</variable>
	
	<!-- <SV offset="9" type="bits" start='0' length='12' name="Set Diverging 3, Address" minValue='0' maxValue='2048' rw="1"/> -->
	<variable item="Signal {$sigIndex} Set Diverging 3, Address" CV="{$CV1 +9}" default="0">
		<splitVal highCV="{$CV1 +10}" upperMask="XXXXXVVV"/>
	</variable>
	<!-- <SV offset="10" type="bits" start='7' length='1' name="Set Diverging 3, Direct order" advanced="1" minVersion='3' rw="1"> -->
	<variable item="Signal {$sigIndex} Set Diverging 3, Use switch order" CV="{$CV1+10}" mask="VXXXXXXX" default="0">
		<qualifier><variableref>Decoder Version</variableref><relation>ge</relation><value>3</value></qualifier>
		<enumVal>
			<enumChoice choice="No, react on normal feedback" value='0'/>
			<enumChoice choice="Yes, handle feedback" value='1'/>
		</enumVal>
	</variable>
	
	
	<xsl:call-template name="SignalGoRulesVar">
		<xsl:with-param name="CV1" select="$CV1+11"/>
		<xsl:with-param name="sigIndex" select="$sigIndex"/>
		<xsl:with-param name="goIndex" select="1"/>
	</xsl:call-template>

</xsl:template>

 <xsl:template name="SignalGoRulesVar">
    <xsl:param name="CV1"/>
    <xsl:param name="sigIndex"/>
    <xsl:param name="goIndex"/>

	<xsl:if test="6 >= $goIndex">

		<!-- <SV offset="11" type="bits" start='0' length='1' name="GO 1, Logic" rw="1" minVersion='1'> -->
		<variable item="Signal {$sigIndex} GO {$goIndex}, Logic" CV="{$CV1}" mask="XXXXXXXV" default="0">
			<enumVal>
				<enumChoice choice="OR" value='0'/>
				<enumChoice choice="AND" value='1'/>
			</enumVal>
		</variable>

		<!--	<SV offset="11" type="bits" start='4' length='4' name="GO 1, Type" rw="1"> -->
		<variable item="Signal {$sigIndex} GO {$goIndex}, Type" CV="{$CV1}" mask="VVVVXXXX" default="0">
			<enumVal>
			  <enumChoice choice="Not used" value="0"/>
			  <enumChoice choice="Sw status" value="1"/>
			  <enumChoice choice="Occ sensor" value="2"/>
			  <enumChoice choice="SE" value="3"/>
			  <enumChoice choice="xRule" value="5"/>
			</enumVal>
		</variable>

		<!-- <SV offset="12" type="bits" start='0' length='12' name="GO 1, Address" minValue='0' maxValue='4095' rw="1"/> -->
		<variable item="Signal {$sigIndex} GO {$goIndex}, Address" CV="{$CV1 +1}" default="0">
			<splitVal highCV="{$CV1 +2}" upperMask="XXXXVVVV"/>
		</variable>

		<!-- <SV offset="13" type="bits" start='6' length='1' name="GO 1, Status" rw="1" minVersion='1'> -->
		<variable item="Signal {$sigIndex} GO {$goIndex}, Status" CV="{$CV1 +2}" mask="XVXXXXXX" default="0">
			<enumVal>
				<enumChoice choice="Thrown/0" value='0'/>
				<enumChoice choice="CLosed/1" value='1'/>
			</enumVal>
		</variable>

		<xsl:call-template name="SignalGoRulesVar">
			<xsl:with-param name="CV1" select="$CV1+3"/>
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex+1"/>
		</xsl:call-template>

	</xsl:if>

</xsl:template>

<xsl:template name="AllSignalVars">
	<xsl:param name="CV1" select="100"/>
	<xsl:param name="sigIndex" select="1"/>

	<xsl:if test="10 >= $sigIndex">

		<xsl:call-template name="SignalVars">
			<xsl:with-param name="CV1" select="$CV1"/>
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
		</xsl:call-template>

		<xsl:call-template name="AllSignalVars">
			<xsl:with-param name="CV1" select="$CV1 +29"/>
			<xsl:with-param name="sigIndex" select="$sigIndex+1"/>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<!-- panes -->

<xsl:template name="SigGoInPane">
	<xsl:param name="sigIndex" select="1"/>
	<xsl:param name="goIndex" select="1"/>

	<xsl:if test="6 >= $goIndex">
	
		<label><text>Signal <xsl:value-of select="$sigIndex"/>, Go rule <xsl:value-of select="$goIndex"/></text></label>
		<display item="Signal {$sigIndex} GO {$goIndex}, Logic">
			<label>Logic</label>
		</display>
		<display item="Signal {$sigIndex} GO {$goIndex}, Type">
			<label>Type</label>
		</display>
		<display item="Signal {$sigIndex} GO {$goIndex}, Address">
			<label>Address</label>
		</display>
		<display item="Signal {$sigIndex} GO {$goIndex}, Status">
			<label>Status</label>
		</display>

		<xsl:call-template name="SigGoInPane">
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex +1"/>
		</xsl:call-template>

	</xsl:if>
	
</xsl:template>

<xsl:template name="AllSignalsInPane">
	<xsl:param name="sigIndex" select="1"/>

	<xsl:if test="11 > $sigIndex">

		<label><text>&#160;</text></label>
		<separator/> 
		<label><text>Signal <xsl:value-of select="$sigIndex"/></text></label>

			<label>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>6</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>7</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>8</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>11</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>12</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>13</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>14</value></qualifier>
				<text layout="right">Type indetermined</text>
			</label>
		

		<display item="SignalSE {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>6</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="SignalDE {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>7</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="SignalDK {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>8</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="SignalCZ {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>11</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="SignalUS {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>12</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="SignalNO {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>13</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="SignalNL {$sigIndex} Type">
		  <qualifier><variableref>Product Id</variableref><relation>eq</relation><value>14</value></qualifier>
		    <label>Type</label>
		</display>
		<display item="Signal {$sigIndex} Default start">
			<label>Default start</label>
		</display>
		<display item="Signal {$sigIndex} First LED number">
			<label>First LED number</label>
		</display>
		<display item="Signal {$sigIndex} Short way">
			<label>Short way</label>
		</display>
		<display item="Signal {$sigIndex} Direction Control">
			<label>Direction Control</label>
		</display>
		<display item="Signal {$sigIndex} Intensity bank number">
			<label>Intensity bank number</label>
		</display>
		<display item="Signal {$sigIndex} Combine with next">
			<label>Combine rule space with next</label>
		</display>
		<display item="Signal {$sigIndex} Combined with previous">
			<label>Combined rule space with previous</label>
		</display>

		<label><text>&#160;</text></label>
		<label><text>Distant signalling</text></label>
		<display item="Signal {$sigIndex} Next signal address">
			<label>Next signal address</label>
		</display>

		<label><text>&#160;</text></label>
		<label><text>Divering switches</text></label>

		<display item="Signal {$sigIndex} Set Diverging 1, Address">
			<label>Set Diverging 1, Address</label>
		</display>
		<display item="Signal {$sigIndex} Set Diverging 1, Use switch order">
			<label>Set Diverging 1, Use switch order</label>
		</display>
		<display item="Signal {$sigIndex} Set Diverging 2, Address">
			<label>Set Diverging 2, Address</label>
		</display>
		<display item="Signal {$sigIndex} Set Diverging 2, Use switch order">
			<label>Set Diverging 2, Use switch order</label>
		</display>
		<display item="Signal {$sigIndex} Set Diverging 3, Address">
			<label>Set Diverging 3, Address</label>
		</display>
		<display item="Signal {$sigIndex} Set Diverging 3, Use switch order">
			<label>Set Diverging 3, Use switch order</label>
		</display>

		<label><text>&#160;</text></label>
		<xsl:call-template name="SigGoInPane">
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
			<xsl:with-param name="goIndex" select="1"/>
		</xsl:call-template>
		
		<label><text>&#160;</text></label>

	</xsl:if>

</xsl:template>

<!-- - - - MATCH - - - -->
<xsl:template match="pane[name='SignalsPane']">
	<pane>
	<name>Signals</name>

	<column>
		<row>
			<display item="Product Id">
				<label>Product Id</label>
			</display>
			<label><text>&#160;&#160;&#160;</text></label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>6</value></qualifier>
				<text>Swedish signal decoder</text>
			</label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>7</value></qualifier>
				<text>German signal decoder</text>
			</label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>8</value></qualifier>
				<text>Danish signal decoder</text>
			</label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>11</value></qualifier>
				<text>Czech signal decoder</text>
			</label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>12</value></qualifier>
				<text>US signal decoder</text>
			</label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>13</value></qualifier>
				<text>Norweigen signal decoder</text>
			</label>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>eq</relation><value>14</value></qualifier>
				<text>Dutch signal decoder</text>
			</label>
		</row>
		<row>
			<label>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>6</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>7</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>8</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>11</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>12</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>13</value></qualifier>
				<qualifier><variableref>Product Id</variableref><relation>ne</relation><value>14</value></qualifier>
				<text>Country for Signal decoder is not defined, please read data from decoder</text>
			</label>
		</row>

		<row>
		<column>

			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="1"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="3"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="5"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="7"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="9"/>
			</xsl:call-template>
		</column>
		<column>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="2"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="4"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="6"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="8"/>
			</xsl:call-template>
			<xsl:call-template name="AllSignalsInPane">
				<xsl:with-param name="sigIndex" select="10"/>
			</xsl:call-template>
		</column>
		</row>
		</column>
	   </pane>
</xsl:template>


<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Special Signal Controls * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<xsl:template name="SpecialSignalVars">
	<xsl:param name="CV1" select="800"/>
	<xsl:param name="sigIndex" select="1"/>
	<xsl:param name="goIndex" select="1"/>


	<xsl:call-template name="SpecialSignalControls">
		<xsl:with-param name="CV1" select="$CV1"/>
		<xsl:with-param name="sigIndex" select="$sigIndex"/>
	</xsl:call-template>

</xsl:template>

 <xsl:template name="SpecialSignalControls">
    <xsl:param name="CV1"/>
    <xsl:param name="sigIndex"/>
    <xsl:param name="goIndex" select="1"/>

	<xsl:if test="4 >= $goIndex">

		<!-- <SV offset="0" type="int2" minValue='0'  maxValue='4096' name="Control Address" rw="1"/> -->
		<variable item="Signal {$sigIndex} Control {$goIndex} Control Address" CV="{$CV1}" default="0">
			<splitVal highCV="{$CV1 +1}" upperMask="XXXXVVVV"/>
		</variable>

		<!-- <SV offset="1" type="bits" start='0' length='1' name="Feedback" rw="1" minVersion='12'> -->
		<variable item="Signal {$sigIndex} Control {$goIndex} Default State" CV="{$CV1+1}" mask="XXXVXXXX" default="0">
			<enumVal>
				<enumChoice choice="Thrown at start" value='0'/>
				<enumChoice choice="Closed at start" value='1'/>
			</enumVal>
		</variable>

		<!-- <SV offset="1" type="bits" start='0' length='1' name="Feedback" rw="1" minVersion='9'> -->
		<variable item="Signal {$sigIndex} Control {$goIndex} Feedback" CV="{$CV1+1}" mask="XXVXXXXX" default="0">
			<enumVal>
				<enumChoice choice="No, react on normal feedback" value='0'/>
				<enumChoice choice="Yes, handle feedback" value='1'/>
			</enumVal>
		</variable>
		
		<!-- <SV offset="1" type="bits" start='0' length='1' name="Special State" rw="1" minVersion='9'> -->
		<variable item="Signal {$sigIndex} Control {$goIndex} Special State" CV="{$CV1+1}" mask="XVXXXXXX" default="0">
			<enumVal>
				<enumChoice choice="Active at thrown" value='0'/>
				<enumChoice choice="Active at closed" value='1'/>
			</enumVal>
		</variable>
		
		<!-- <SV offset="1" type="bits" start='0' length='1' name="Force View" rw="1" minVersion='9'> -->
		<variable item="Signal {$sigIndex} Control {$goIndex} Force View" CV="{$CV1+1}" mask="VXXXXXXX" default="0">
			<enumVal>
				<enumChoice choice="No,view when alloved" value='0'/>
				<enumChoice choice="Yes, force this view" value='1'/>
			</enumVal>
		</variable>

		<xsl:call-template name="SpecialSignalControls">
			<xsl:with-param name="CV1" select="$CV1+2"/>
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex+1"/>
		</xsl:call-template>

	</xsl:if>

</xsl:template>

<xsl:template name="AllSpecialSignalVars">
	<xsl:param name="CV1" select="800"/>
	<xsl:param name="sigIndex" select="1"/>

	<xsl:if test="10 >= $sigIndex">
	
		<xsl:call-template name="SpecialSignalVars">
			<xsl:with-param name="CV1" select="$CV1"/>
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
		</xsl:call-template>
		
	<!-- <SV offset="8" type="bits" start='0' length='4' name="Control 1 Address Type" rw="1" minVersion='11'> -->
	<variable item="Signal {$sigIndex} Control 1 Address Type" CV="{$CV1+8}" mask="XXXXVVVV" default="0">
		<enumVal>
			<enumChoice choice="Not used" value='0'/>
			<enumChoice choice="Switch Status" value='1'/>
			<enumChoice choice="Track Sensor" value='2'/>
			<enumChoice choice="Signal Status" value='3'/>
			<enumChoice choice="Extra Rule" value='4'/>
			<enumChoice choice="Fixed Setting" value='5'/>
		</enumVal>
	</variable>
	
	<!-- <SV offset="8" type="bits" start='4' length='4' name="Control 2 Address Type" rw="1" minVersion='11'> -->
	<variable item="Signal {$sigIndex} Control 2 Address Type" CV="{$CV1+8}" mask="VVVVXXXX" default="0">
		<enumVal>
			<enumChoice choice="Not used" value='0'/>
			<enumChoice choice="Switch Status" value='1'/>
			<enumChoice choice="Track Sensor" value='2'/>
			<enumChoice choice="Signal Status" value='3'/>
			<enumChoice choice="Extra Rule" value='4'/>
			<enumChoice choice="Fixed Setting" value='5'/>
		</enumVal>
	</variable>
	
		
	<!-- <SV offset="9" type="bits" start='0' length='4' name="Control 3 Address Type" rw="1" minVersion='11'> -->
	<variable item="Signal {$sigIndex} Control 3 Address Type" CV="{$CV1+9}" mask="XXXXVVVV" default="0">
		<enumVal>
			<enumChoice choice="Not used" value='0'/>
			<enumChoice choice="Switch Status" value='1'/>
			<enumChoice choice="Track Sensor" value='2'/>
			<enumChoice choice="Signal Status" value='3'/>
			<enumChoice choice="Extra Rule" value='4'/>
			<enumChoice choice="Fixed Setting" value='5'/>
		</enumVal>
	</variable>
	
	<!-- <SV offset="9" type="bits" start='4' length='4' name="Control 4 Address Type" rw="1" minVersion='11'> -->
	<variable item="Signal {$sigIndex} Control 4 Address Type" CV="{$CV1+9}" mask="VVVVXXXX" default="0">
		<enumVal>
			<enumChoice choice="Not used" value='0'/>
			<enumChoice choice="Switch Status" value='1'/>
			<enumChoice choice="Track Sensor" value='2'/>
			<enumChoice choice="Signal Status" value='3'/>
			<enumChoice choice="Extra Rule" value='4'/>
			<enumChoice choice="Fixed Setting" value='5'/>
		</enumVal>
	</variable>
	

	<xsl:call-template name="AllSpecialSignalVars">
			<xsl:with-param name="CV1" select="$CV1+10"/>
			<xsl:with-param name="sigIndex" select="$sigIndex+1"/>
		</xsl:call-template>
	
	</xsl:if>
</xsl:template>

<!-- panes -->

<xsl:template name="SigSpecialInPane">
	<xsl:param name="sigIndex" select="1"/>
	<xsl:param name="goIndex" select="1"/>

	<xsl:if test="4 >= $goIndex">
	
		<label><text>Signal <xsl:value-of select="$sigIndex"/>, Control <xsl:value-of select="$goIndex"/></text></label>

		<display item="Signal {$sigIndex} Control {$goIndex} Control Address">
			<label>Control Address</label>
		</display>
		<display item="Signal {$sigIndex} Control {$goIndex} Default State">
			<label>Default State</label>
		</display>
		<display item="Signal {$sigIndex} Control {$goIndex} Feedback">
			<label>Feedback</label>
		</display>
		<display item="Signal {$sigIndex} Control {$goIndex} Special State">
			<label>Special State</label>
		</display>
		<display item="Signal {$sigIndex} Control {$goIndex} Force View">
			<label>Force View</label>
		</display>

		<xsl:call-template name="SigSpecialInPane">
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
			<xsl:with-param name="goIndex" select="$goIndex +1"/>
		</xsl:call-template>

	</xsl:if>
	
</xsl:template>

<xsl:template name="AllSpecialSignalsInPane">
	<xsl:param name="sigIndex" select="1"/>

	<xsl:if test="11 > $sigIndex">

-		<label><text>&#160;</text></label>
		<label><text>Signal <xsl:value-of select="$sigIndex"/></text></label>
		<label><text>&#160;</text></label>

		<display item="Signal {$sigIndex} Control 1 Address Type">
			<label>Control 1 Address Type</label>
		</display>
		<display item="Signal {$sigIndex} Control 2 Address Type">
			<label>Control 2 Address Type</label>
		</display>
		<display item="Signal {$sigIndex} Control 3 Address Type">
			<label>Control 3 Address Type</label>
		</display>
		<display item="Signal {$sigIndex} Control 4 Address Type">
			<label>Control 4 Address Type</label>
		</display>
		
		<label><text>&#160;</text></label>
		<xsl:call-template name="SigSpecialInPane">
			<xsl:with-param name="sigIndex" select="$sigIndex"/>
			<xsl:with-param name="goIndex" select="1"/>
		</xsl:call-template>

	</xsl:if>

</xsl:template>

<!-- - - - MATCH - - - -->
<xsl:template match="pane[name='SigSpecialsPane']">
	<pane>
	<name>Special Signal Controls</name>
		<column>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="1"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="3"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="5"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="7"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="9"/>
			</xsl:call-template>
		</column>
		<column>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="2"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="4"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="6"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="8"/>
			</xsl:call-template>
			<xsl:call-template name="AllSpecialSignalsInPane">
				<xsl:with-param name="sigIndex" select="10"/>
			</xsl:call-template>
		</column>
   </pane>
</xsl:template>

<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * Run All   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->
<!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  -->

<!-- - - - MATCH - - - -->
<!-- install new variables at end of variables element-->
 <xsl:template match="variables">
   <variables>
     <xsl:copy-of select="node()"/>
     <xsl:call-template name="AllSignalVars"/>
     <xsl:call-template name="AllSpecialSignalVars"/>	 
     <xsl:call-template name="AllExtraRuleVar"/>
     <xsl:call-template name="AllShortSignalVars"/>
     <xsl:call-template name="AllRepeatSignalVars"/>
     <xsl:call-template name="AllDirectionDetectionVars"/>
     <xsl:call-template name="AllTriggerRuleVars"/>
     <xsl:call-template name="AllSignalSelectorsVars"/>	 
   </variables>
 </xsl:template>


<!--Identity template copies content forward -->
<!-- - - - MATCH - - - -->
 <xsl:template match="@*|node()">
   <xsl:copy>
     <xsl:apply-templates select="@*|node()"/>
   </xsl:copy>
 </xsl:template>
 
</xsl:stylesheet>
