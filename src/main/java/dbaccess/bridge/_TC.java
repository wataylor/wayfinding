package dbaccess.bridge;
public class _TC {
  private static final long serialVersionUID=1;

/* Constants for referencing the AttributeType table */
public static final short AT_String=1;
public static final short AT_Number=2;
public static final short AT_Image=3;
public static final short AT_URL=4;
public static final String[] AttributeTypeNames ={
"Unk AttributeType", "String", "Number", "Image", "URL", };

/* Constants for referencing the AddressType table */
public static final short AdT_Home=1;
public static final short AdT_Work=2;
public static final short AdT_Billing=3;
public static final short AdT_Shipping=4;
public static final short AdT_Courier=5;
public static final short AdT_Entrance=6;
public static final short AdT_Main_Entrance=7;
public static final short AdT_Emergency_Exit=8;
public static final short AdT_Handicapped=9;
public static final String[] AddressTypeNames ={
"Unk AddressType", "Home", "Work", "Billing", "Shipping", "Courier", "Entrance", "Main Entrance", "Emergency Exit", "Handicapped", };

/* Constants for referencing the DeviceType table */
public static final short DT_Smoke=1;
public static final short DT_Temperature=2;
public static final short DT_Level=3;
public static final short DT_Cutoff=4;
public static final short DT_Alarm_Entry=5;
public static final String[] DeviceTypeNames ={
"Unk DeviceType", "Smoke", "Temperature", "Level", "Cutoff", "Alarm Entry", };

/* Constants for referencing the EntityType table */
public static final short ET_Building=1;
public static final short ET_Floor=2;
public static final short ET_Space=3;
public static final short ET_Location=4;
public static final short ET_Device=5;
public static final short ET_Building_Group=6;
public static final short ET_Address=7;
public static final short ET_Phone_Number=8;
public static final short ET_Permission=9;
public static final short ET_Organization=10;
public static final short ET_Person=11;
public static final short ET_Entity_Relation=12;
public static final String[] EntityTypeNames ={
"Unk EntityType", "Building", "Floor", "Space", "Location", "Device", "Building Group", "Address", "Phone Number", "Permission", "Organization", "Person", "Entity Relation", };

/* Constants for referencing the GroupType table */
public static final short GT_Entertainment=1;
public static final short GT_Transportation=2;
public static final short GT_Municipal=3;
public static final short GT_Manufacturing=4;
public static final short GT_Retail=5;
public static final short GT_Office=6;
public static final short GT_Educational=7;
public static final short GT_Media=8;
public static final short GT_Food=9;
public static final String[] GroupTypeNames ={
"Unk GroupType", "Entertainment", "Transportation", "Municipal", "Manufacturing", "Retail", "Office", "Educational", "Media", "Food", };

/* Constants for referencing the LocationType table */
public static final short LT_Door=1;
public static final short LT_Alarm=2;
public static final short LT_Building_Exit=3;
public static final short LT_Emergency_Exit=4;
public static final short LT_Handicap_Access=5;
public static final String[] LocationTypeNames ={
"Unk LocationType", "Door", "Alarm", "Building Exit", "Emergency Exit", "Handicap Access", };

/* Constants for referencing the HappeningType table */
public static final short HT_Non_Alarm=1;
public static final short HT_Alarm=2;
public static final short HT_Alarm_Clear=3;
public static final short HT_Print=4;
public static final short HT_Notify=5;
public static final short HT_Panel_Error=6;
public static final short HT_System_Info=7;
public static final String[] HappeningTypeNames ={
"Unk HappeningType", "Non-Alarm", "Alarm", "Alarm Clear", "Print", "Notify", "Panel Error", "System Info", };

/* Constants for referencing the OrganizationType table */
public static final short OT_Entertainment=1;
public static final short OT_Transportation=2;
public static final short OT_Municipal=3;
public static final short OT_Manufacturing=4;
public static final short OT_Retail=5;
public static final short OT_Office=6;
public static final short OT_Educational=7;
public static final short OT_Media=8;
public static final short OT_Nonprofit=9;
public static final String[] OrganizationTypeNames ={
"Unk OrganizationType", "Entertainment", "Transportation", "Municipal", "Manufacturing", "Retail", "Office", "Educational", "Media", "Nonprofit", };

/* Constants for referencing the PermissionType table */
public static final short PT_Modify_Building=1;
public static final short PT_Modify_Floor=2;
public static final short PT_Modify_Location=3;
public static final short PT_Modify_Device=4;
public static final short PT_Modify_Person=5;
public static final short PT_Add_Building=6;
public static final short PT_Add_Floor=7;
public static final short PT_Add_Location=8;
public static final short PT_Add_Device=9;
public static final short PT_Add_Person=10;
public static final String[] PermissionTypeNames ={
"Unk PermissionType", "Modify Building", "Modify Floor", "Modify Location", "Modify Device", "Modify Person", "Add Building", "Add Floor", "Add Location", "Add Device", "Add Person", };

/* Constants for referencing the PhoneNumberType table */
public static final short PnT_Home=1;
public static final short PnT_Work=2;
public static final short PnT_Cell=3;
public static final String[] PhoneNumberTypeNames ={
"Unk PhoneNumberType", "Home", "Work", "Cell", };

/* Constants for referencing the SpaceType table */
public static final short ST_Hall=1;
public static final short ST_Vertical=2;
public static final short ST_Room=3;
public static final short ST_Door=4;
public static final short ST_Location=5;
public static final short ST_Stair=6;
public static final short ST_Toilet=7;
public static final short ST_Ramp=8;
public static final short ST_Elevator=9;
public static final short ST_Lobby=10;
public static final short ST_Atrium=11;
public static final String[] SpaceTypeNames ={
"Unk SpaceType", "Hall", "Vertical", "Room", "Door", "Location", "Stair", "Toilet", "Ramp", "Elevator", "Lobby", "Atrium", };

/* Constants for referencing the TermType table */
public static final short TT_Term_Type=1;
public static final short TT_Language=2;
public static final short TT_Country=3;
public static final short TT_State=4;
public static final short TT_Entity=5;
public static final short TT_Address=6;
public static final short TT_Phone_Number=7;
public static final short TT_Group=8;
public static final short TT_Organization=9;
public static final short TT_Permission=10;
public static final short TT_Attribute=11;
public static final short TT_Space=12;
public static final short TT_Location=13;
public static final short TT_Device=14;
public static final short TT_Noun=15;
public static final short TT_Verb=16;
public static final short TT_Phrase=17;
public static final short TT_Message=18;
public static final short TT_Unit=19;
public static final short TT_Event_Type=20;
public static final String[] TermTypeNames ={
"Unk TermType", "Term Type", "Language", "Country", "State", "Entity", "Address", "Phone Number", "Group", "Organization", "Permission", "Attribute", "Space", "Location", "Device", "Noun", "Verb", "Phrase", "Message", "Unit", "Event Type", };
}
