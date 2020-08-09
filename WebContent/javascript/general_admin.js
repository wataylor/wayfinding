/* START TOOL TIP STRINGS */
//Global Tips
tip_currentBldg="This section displays the name and/or street address of the building you are currently administering.";
tip_addCustomField="msg here...";
tip_delete="Delete";
tip_login="msg here..."

//Building Info Page Tips
tip_bldgSearch="msg here...";
tip_bldgDefaultInfo="msg here...";
tip_bldgContacts="msg here...";

//Location Info Page Tips
tip_currentBldgLoco = "This section displays the name and/or street address of the building you are currently administering, <br /> as well as the selected location inside the building.";
tip_locationSearch="Type the name, keyword or other descriptive term that describes the location you wish to find and <br/> then select the location from the generated list. Use the Filter Menu on the right to limit your seach results.";
tip_locationDefaultInfo="msg here...";
tip_locationAdditionalInfo="msg here...";
tip_locationAddCompany="msg here...";
tip_locationPeopleAssiged="msg here...";
tip_locationPeopleUnAssiged="msg here...";
tip_locationCompanyAssiged="msg here...";
tip_locationCompanyUnAssiged="msg here...";
tip_locationUploadPhotos="msg here...";
tip_locationCurrentPhotos="msg here...";

//Personnel Page Tips
tip_personnelSearch="msg here...";
tip_personnelDefaultInfo="msg here...";
tip_personnelAdditionalInfo="msg here...";

//Company Page Tips
tip_companySearch="msg here...";
tip_companyDefaultInfo="msg here...";
tip_companyAdditionalInfo="msg here...";

//Reports Page Tips
tip_startDate="msg here...";
tip_endDate="msg here...";
tip_reportResults="msg here...";

//Updates Page Tips
tip_availableUpdates="msg here...";
tip_maualUpdates="msg here...";
tip_softwareUpdates="msg here...";
tip_cadUpdates="msg here...";

//Bulk Uploads Page Tips
tip_uploadTemplates="msg here...";
tip_uploadImages="msg here...";
tip_bulkTemplates="msg here...";
/* END TOOL TIP STRINGS */

/* ADDS ROUNDED CORNERS TO BLDGS NAME TAB */
	window.onload=function(){
		Nifty("div#way_loginContentContainer","big");
		Nifty("div#way_globalNavMenu","bottom, big");
	};

/* POPUP WINDOW FUNCTION CALL AND WINDOW PROFILES */
	var profiles =  {
		mapWindow:{height:600,width:800,scrollbars:1,center:0}

	};

	$(function(){
	  $(".popupwindow").popupwindow(profiles);
	});

/* JQUERY CALLS */
$(document).ready(function (){

	<!-- MAIN NAV TAB SWITCHER -->
	/*$("#way_adminMainMenu a").click(function() {
		if ($("#way_adminMainMenu a.current").length > 0){
			$("#way_adminMainMenu a.current").removeClass('current');}
		$(this).addClass('current');
		});*/

    <!-- SHOW SEARCH OPTIONS -->
	$("a.hideSrchOptsLink").click(function() {
		$("a.srchOptsLink").show();$(".admin_srchNoOptsBox").show();$("a.hideSrchOptsLink").hide();$(".admin_srchWithOptsBox").hide();
	    });

	<!-- HIDE SEARCH OPTIONS -->
	$("a.srchOptsLink").click(function() {
		$("a.srchOptsLink").hide();$(".admin_srchNoOptsBox").hide();$("a.hideSrchOptsLink").show();$(".admin_srchWithOptsBox").show();
	    });

	<!-- SUB NAV TAB SWITCHER -->
	$("#way_adminSubMenu a").click(function() {
		if ($("#way_adminSubMenu a.current").length > 0){
			$("#way_adminSubMenu a.current").removeClass('current');}
		$(this).addClass('current');
		});

	<!-- SHOW LOCATION GENERAL INFO FORM -->
	$("a#tabGeneral").click(function() {
		$("#locationGeneralContent").show();$("#locationPersonnelContent").hide();$("#locationCompanyContent").hide();$("#locationImageContent").hide();$("#locationCameraContent").hide();$("#locationBulkContent").hide();
	    });
	<!-- SHOW LOCATION ASSIGN PERSONNEL FORM -->
	$("a#tabAssignPersonnel").click(function() {
		$("#locationGeneralContent").hide();$("#locationPersonnelContent").show();$("#locationCompanyContent").hide();$("#locationImageContent").hide();$("#locationCameraContent").hide();$("#locationBulkContent").hide();
		Location.goingToAssignPersonnel();
	    });
	<!-- SHOW LOCATION ASSIGN COMPANY FORM -->
	$("a#tabAssignCompany").click(function() {
		$("#locationGeneralContent").hide();$("#locationPersonnelContent").hide();$("#locationCompanyContent").show();$("#locationImageContent").hide();$("#locationCameraContent").hide();$("#locationBulkContent").hide();
		Location.goingToAssignCompany();
	    });
	<!-- SHOW LOCATION PHOTO FORM -->
	$("a#tabImages").click(function() {
		$("#locationGeneralContent").hide();$("#locationPersonnelContent").hide();$("#locationCompanyContent").hide();$("#locationImageContent").show();$("#locationCameraContent").hide();$("#locationBulkContent").hide();
	    });
	<!-- SHOW LOCATION CAMERAS INFO FORM -->
	$("a#tabCameras").click(function() {
		$("#locationGeneralContent").hide();$("#locationPersonnelContent").hide();$("#locationCompanyContent").hide();$("#locationImageContent").hide();$("#locationCameraContent").show();$("#locationBulkContent").hide();
	    });

	<!-- SHOW BULK UPLOAD FORM -->
	$("a#tabUpload").click(function() {
		$("#bulkUploadContent").show();$("#bulkTemplatesContent").hide();
	    });
	<!-- SHOW BULK TEMPLATES -->
	$("a#tabTemplates").click(function() {
		$("#bulkUploadContent").hide();$("#bulkTemplatesContent").show();
		});

	<!-- SHOW AUTO UPDATES INFO -->
	$("a#tabAutoUpdates").click(function() {
		$("#autoUpdateContent").show();$("#manualUpdateContent").hide();
	    });
	<!-- SHOW MANUAL UPDATES FORM -->
	$("a#tabManualUpdates").click(function() {
		$("#autoUpdateContent").hide();$("#manualUpdateContent").show();
		});
});

<!-- FUNCTION TO SHOW/HIDE CUSTOM FORM FILEDS BASED ON SELECTION -->
function custFieldSelect(){
 //alert($("select#addFieldType option:selected").val());
 var selectedCustomField = $("select#addFieldType option:selected").val();
	 switch (selectedCustomField){
	 case 'customInputBox':
		 $("#addInputBoxField").show();$("#addCheckBoxField").hide();$("#addRadioBtnField").hide();$("#addTextAreaField").hide();$("#addSelectMenuField").hide();
		 break;
	 case 'customCheckBox':
		 $("#addInputBoxField").hide();$("#addCheckBoxField").show();$("#addRadioBtnField").hide();$("#addTextAreaField").hide();$("#addSelectMenuField").hide();
		 break;
	 case 'customRadioBtn':
		 $("#addInputBoxField").hide();$("#addCheckBoxField").hide();$("#addRadioBtnField").show();$("#addTextAreaField").hide();$("#addSelectMenuField").hide();
		 break;
	 case 'customTextArea':
		 $("#addInputBoxField").hide();$("#addCheckBoxField").hide();$("#addRadioBtnField").hide();$("#addTextAreaField").show();$("#addSelectMenuField").hide();
		 break;
	 case 'customSelectBox':
		 $("#addInputBoxField").hide();$("#addCheckBoxField").hide();$("#addRadioBtnField").hide();$("#addTextAreaField").hide();$("#addSelectMenuField").show();
		 break;
	 default:
		 $("#addInputBoxField").hide();$("#addCheckBoxField").hide();$("#addRadioBtnField").hide();$("#addTextAreaField").hide();$("#addSelectMenuField").hide();
		 break;
	 }
};
