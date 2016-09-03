/**
 * Created by benjamingarner on 14/08/2016.
 */
import java.util.HashMap;
import java.util.Map;

public class destinytabledefinitions {

    private HashMap destinyTableDefinitionMap = new HashMap<String,String>();

    public destinytabledefinitions()
    {
        destinyTableDefinitionMap.put("ActivityBundle","DestinyActivityBundleDefinition");
        destinyTableDefinitionMap.put("ActivityCategory","DestinyActivityCategoryDefinition");
        destinyTableDefinitionMap.put("Activity","DestinyActivityDefinition");
        destinyTableDefinitionMap.put("ActivityType","DestinyActivityTypeDefinition");
        destinyTableDefinitionMap.put("Bond","DestinyBondDefinition");
        destinyTableDefinitionMap.put("Class","DestinyClassDefinition");
        destinyTableDefinitionMap.put("Combatant","DestinyCombatantDefinition");
        destinyTableDefinitionMap.put("DamageType","DestinyDamageTypeDefinition");
        destinyTableDefinitionMap.put("Destination","DestinyDestinationDefinition");
        destinyTableDefinitionMap.put("DirectorBook","DestinyDirectorBookDefinition");
        destinyTableDefinitionMap.put("EnemyRace","DestinyEnemyRaceDefinition");
        destinyTableDefinitionMap.put("Faction","DestinyFactionDefinition");
        destinyTableDefinitionMap.put("Gender","DestinyGenderDefinition");
        destinyTableDefinitionMap.put("GrimoireCard","DestinyGrimoireCardDefinition");
        destinyTableDefinitionMap.put("Grimoire","DestinyGrimoireDefinition");
        destinyTableDefinitionMap.put("HistoricalStats","DestinyHistoricalStatsDefinition");
        destinyTableDefinitionMap.put("InventoryBucket","DestinyInventoryBucketDefinition");
        destinyTableDefinitionMap.put("InventoryItem","DestinyInventoryItemDefinition");
        destinyTableDefinitionMap.put("ItemCategory","DestinyItemCategoryDefinition");
        destinyTableDefinitionMap.put("Location","DestinyLocationDefinition");
        destinyTableDefinitionMap.put("Objective","DestinyObjectiveDefinition");
        destinyTableDefinitionMap.put("Place","DestinyPlaceDefinition");
        destinyTableDefinitionMap.put("Progression","DestinyProgressionDefinition");
        destinyTableDefinitionMap.put("Race","DestinyRaceDefinition");
        destinyTableDefinitionMap.put("RecordBook","DestinyRecordBookDefinition");
        destinyTableDefinitionMap.put("Record","DestinyRecordDefinition");
        destinyTableDefinitionMap.put("RewardSource","DestinyRewardSourceDefinition");
        destinyTableDefinitionMap.put("SandboxPerk","DestinySandboxPerkDefinition");
        destinyTableDefinitionMap.put("ScriptedSkull","DestinyScriptedSkullDefinition");
        destinyTableDefinitionMap.put("SpecialEvent","DestinySpecialEventDefinition");
        destinyTableDefinitionMap.put("Stat","DestinyStatDefinition");
        destinyTableDefinitionMap.put("TalentGrid","DestinyTalentGridDefinition");
        destinyTableDefinitionMap.put("TriumphSet","DestinyTriumphSetDefinition");
        destinyTableDefinitionMap.put("UnlockFlag","DestinyUnlockFlagDefinition");
        destinyTableDefinitionMap.put("VendorCategory","DestinyVendorCategoryDefinition");
        destinyTableDefinitionMap.put("Vendor","DestinyVendorDefinition");
    }

    public Object getDefinition(String key)
    {
        return destinyTableDefinitionMap.get(key);
    }

}
