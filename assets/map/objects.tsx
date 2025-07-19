<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="objects" tilewidth="96" tileheight="93" tilecount="53" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN_SWORD"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="1.1"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image source="../graphics/objects/captain_clown_sword.png" width="64" height="40"/>
  <objectgroup draworder="index" id="3">
   <object id="20" x="24" y="16" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="26" x="25" y="18">
    <properties>
     <property name="density" type="float" value="1.75"/>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,12 2,13 12.8125,13 14,12 14,0"/>
   </object>
   <object id="27" x="29" y="30">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="0,0 0,1 6,1 6,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="29">
  <properties>
   <property name="animFrameDuration" type="float" value="0.16"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="CHEST"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="itemsInside" value="GOLD_COIN:3,SILVER_COIN:2,SWORD:1"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/chest.png" width="64" height="35"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="18" y="10">
    <properties>
     <property name="userData" value="chest"/>
    </properties>
    <polygon points="0,0 0,25 30,25 30,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="30">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FLAG"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/flag.png" width="34" height="93"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="4" height="93">
    <properties>
     <property name="userData" value="flag"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="31">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SHIP_HELM"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/ship_helm.png" width="31" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="4" y="4" width="23" height="23">
    <properties>
     <property name="userData" value="shipHelm"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="32">
  <properties>
   <property name="damage" type="int" value="1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SPIKES"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/spikes.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="9" x="8" y="16" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="12" x="16" y="19" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="13" x="24" y="17" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="0.4"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="14" x="0" y="21" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="34">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="RESPAWNABLE"/>
   <property name="gameObject" propertytype="GameObject" value="SWORD"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/sword.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="1" y="10">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="sword"/>
    </properties>
    <polygon points="0,0 4,-3 19,-2 19,1 17,3 7,3 3,4 0,3"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="35">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="1.1"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image source="../graphics/objects/captain_clown.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="24" x="24" y="16" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="25" x="25" y="18">
    <properties>
     <property name="density" type="float" value="1.75"/>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,13 14,13 14,0"/>
   </object>
   <object id="26" x="29" y="30">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="0,0 0,1 6,1 6,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="48">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="circleRadius" type="float" value="3"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="CRABBY"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="1.1"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="1"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image source="../graphics/objects/crabby.png" width="72" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="27" y="13" width="20" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="5" x="25" y="8" width="22" height="22">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
   <object id="8" x="28" y="15">
    <properties>
     <property name="density" type="float" value="3"/>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,12 18,12 18,0"/>
   </object>
   <object id="9" x="33" y="25">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="0,0 0,2 9,2 9,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="49">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="attack" type="int" value="0"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="circleRadius" type="float" value="4"/>
   <property name="critChance" type="float" value="0"/>
   <property name="critDamage" type="float" value="0"/>
   <property name="defence" type="int" value="0"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="FIERCE_TOOTH"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="resistance" type="int" value="0"/>
   <property name="speed" type="int" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image source="../graphics/objects/fierce_tooth.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="9" y="7" width="17" height="20">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="4" x="7" y="6" width="22" height="22">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
   <object id="6" x="10" y="9">
    <properties>
     <property name="density" type="float" value="2"/>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,17 15,17 15,0"/>
   </object>
   <object id="8" x="13" y="24">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="0,0 0,2 9,2 9,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="50">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="circleRadius" type="float" value="7"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="PINK_STAR"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="3"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image source="../graphics/objects/pink_star.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10" y="10" width="14" height="18">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="4" x="6" y="7" width="22" height="22">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
   <object id="10" x="11" y="13">
    <properties>
     <property name="density" type="float" value="1.25"/>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,11 3,14 10,14 12,11 12,0"/>
   </object>
   <object id="11" x="14" y="25">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="0,0 0,2 7,2 7,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="51">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND,ATTACH_TO_SHIP"/>
   <property name="gameObject" propertytype="GameObject" value="ANCHOR"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/anchor.png" width="19" height="18"/>
  <objectgroup draworder="index" id="3">
   <object id="2" x="7" y="0">
    <properties>
     <property name="userData" value="anchor"/>
    </properties>
    <polygon points="0,0 -7,6 -7,12 -1,18 6,18 12,12 12,6 5,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="52">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_LEFT"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/back_palm_tree_left.png" width="51" height="53"/>
 </tile>
 <tile id="53">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_REGULAR"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/back_palm_tree_regular.png" width="64" height="64"/>
 </tile>
 <tile id="54">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_RIGHT"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/back_palm_tree_right.png" width="52" height="53"/>
 </tile>
 <tile id="55">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="BARREL"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="isFixedRotation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/barrel.png" width="26" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="5" y="3">
    <properties>
     <property name="density" type="float" value="1.75"/>
     <property name="userData" value="barrel"/>
    </properties>
    <polygon points="0,0 -4,6 -4,19 0,25 15,25 19,18 19,6 15,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="56">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="BOX"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="isFixedRotation" type="bool" value="false"/>
   <property name="itemsInside" type="int" value="1"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/box.png" width="28" height="22"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="1" y="2">
    <properties>
     <property name="density" type="float" value="1.5"/>
     <property name="friction" type="float" value="0.5"/>
     <property name="userData" value="box"/>
    </properties>
    <polygon points="0,18 25,18 25,0 0,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="57">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="CHEST_LOCKED"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="itemsInside" value="GOLD_COIN:3,SILVER_COIN:2,SWORD:1"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/chest_locked.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="1" y="8">
    <properties>
     <property name="userData" value="chest_locked"/>
    </properties>
    <polygon points="0,0 0,24 30,24 30,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="58">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FRONT_PALM_TREE"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/front_palm_tree.png" width="39" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="5" y="3" width="29" height="6">
    <properties>
     <property name="userData" value="platform"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="59">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="KEY"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/key.png" width="24" height="24"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="8" y="5" width="8" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="key"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="60">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PADLOCK"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/padlock.png" width="32" height="32"/>
 </tile>
 <tile id="61">
  <properties>
   <property name="animFrameDuration" type="float" value="0"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_LEFT"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/palm_tree_bottom_left.png" width="64" height="32"/>
 </tile>
 <tile id="62">
  <properties>
   <property name="animFrameDuration" type="float" value="0"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_REGULAR"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/palm_tree_bottom_regular.png" width="32" height="32"/>
 </tile>
 <tile id="63">
  <properties>
   <property name="animFrameDuration" type="float" value="0"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_RIGHT"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/palm_tree_bottom_right.png" width="64" height="30"/>
 </tile>
 <tile id="64">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="WATER"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="REFLEXES_1"/>
  </properties>
  <image source="../graphics/objects/reflexes_1.png" width="56" height="36"/>
 </tile>
 <tile id="65">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="WATER"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="REFLEXES_2"/>
  </properties>
  <image source="../graphics/objects/reflexes_2.png" width="56" height="36"/>
 </tile>
 <tile id="66">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND,ATTACH_TO_SHIP"/>
   <property name="gameObject" propertytype="GameObject" value="SAIL"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="WIND"/>
  </properties>
  <image source="../graphics/objects/sail.png" width="28" height="50"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="1" y="0" width="4" height="50">
    <properties>
     <property name="userData" value="sail"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="67">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SHIP"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/ship.png" width="80" height="26"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="79" y="4">
    <properties>
     <property name="userData" value="ship"/>
    </properties>
    <polygon points="0,0 -11,5 -28,19 -78,19 -78,-2 0,-2"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="68">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="WATER"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="SPLASH_1"/>
  </properties>
  <image source="../graphics/objects/splash_1.png" width="24" height="12"/>
 </tile>
 <tile id="69">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="WATER"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="SPLASH_2"/>
  </properties>
  <image source="../graphics/objects/splash_2.png" width="24" height="12"/>
 </tile>
 <tile id="71">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="WATER"/>
   <property name="hasAnimation" type="bool" value="false"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/water.png" width="96" height="32"/>
 </tile>
 <tile id="72">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="BIG_MAP"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/big_map.png" width="30" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="2" width="30" height="30">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="bigMap"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="73">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="BLUE_DIAMOND"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/blue_diamond.png" width="24" height="24"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="8" y="6">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="blueDiamond"/>
    </properties>
    <polygon points="0,0 8,0 11,3 11,6 4,12 -3,6 -3,3"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="74">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="BLUE_POTION"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/blue_potion.png" width="13" height="17"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="3" y="4" width="7" height="13">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="bluePotion"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="75">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="GOLD_COIN"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/gold_coin.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="3" y="3" width="11" height="11">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="goldCoin"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="76">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="GOLDEN_SKULL"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/golden_skull.png" width="24" height="28"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="8" y="13">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="goldenSkull"/>
    </properties>
    <polygon points="0,0 10,0 13,4 13,8 9,13 1,13 -3,7 -3,4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="77">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="GREEN_BOTTLE"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/green_bottle.png" width="13" height="17"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="8">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="greenBottle"/>
    </properties>
    <polygon points="0,0 4,-8 10,-8 13,1 13,9 0,9"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="78">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="GREEN_DIAMOND"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/green_diamond.png" width="24" height="24"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="6" y="6" width="12" height="12">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="greenDiamond"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="79">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="RED_DIAMOND"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/red_diamond.png" width="24" height="24"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="11" y="4">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="redDiamond"/>
    </properties>
    <polygon points="0,0 -7,7 0,15 8,7"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="80">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="RED_POTION"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/red_potion.png" width="13" height="17"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="5" y="3">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="redPotion"/>
    </properties>
    <polygon points="0,0 -3,8 -3,12 -1,14 4,14 6,12 6,8 3,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="81">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SILVER_COIN"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/silver_coin.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="3" y="3" width="11" height="11">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="silverCoin"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="82">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SMALL_MAP_1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/small_map_1.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="2" y="3" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="smallMap1"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="83">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SMALL_MAP_2"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/small_map_2.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="2" y="3" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="smallMap2"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="84">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SMALL_MAP_3"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/small_map_3.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="2" y="3" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="smallMap3"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="85">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SMALL_MAP_4"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/small_map_4.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="2" y="3" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="smallMap4"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="86">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="circleRadius" type="float" value="8"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="TOTEM_HEAD_1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="life" type="int" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/totem_head_1.png" width="60" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="16" y="9" width="20" height="22">
    <properties>
     <property name="userData" value="totemHead1"/>
    </properties>
   </object>
   <object id="4" x="16" y="25">
    <properties>
     <property name="userData" value="totemHead1"/>
    </properties>
    <polygon points="0,0 -7,0 -15,-7 -15,-10 0,-10"/>
   </object>
   <object id="5" x="36" y="14">
    <properties>
     <property name="userData" value="totemHead1"/>
    </properties>
    <polygon points="0,0 0,14 4,14 19,9 23,4 23,1 9,1 9,0"/>
   </object>
   <object id="6" x="14" y="9" width="24" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="8"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="87">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="circleRadius" type="float" value="8"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="TOTEM_HEAD_2"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="life" type="int" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/totem_head_2.png" width="24" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="1" y="10" width="21" height="22">
    <properties>
     <property name="userData" value="totemHead2"/>
    </properties>
   </object>
   <object id="2" x="-1" y="10" width="24" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="8"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="88">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="circleRadius" type="float" value="8"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="TOTEM_HEAD_3"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="life" type="int" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/totem_head_3.png" width="30" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="6" y="10" width="23" height="22">
    <properties>
     <property name="userData" value="totemHead3"/>
    </properties>
   </object>
   <object id="2" x="6" y="10" width="24" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="8"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="89">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="circleRadius" type="float" value="8"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="TOTEM_HEAD_4"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="life" type="int" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/totem_head_4.png" width="60" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="17" y="31">
    <properties>
     <property name="userData" value="totemHead4"/>
    </properties>
    <polygon points="0,0 16,0 40,-10 41,-16 18,-17 18,-22 -2,-22 -2,-17 -17,-16 -14,-9"/>
   </object>
   <object id="2" x="14" y="10" width="24" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="8"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="90">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="circleRadius" type="float" value="8"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="TOTEM_HEAD_5"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="life" type="int" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/totem_head_5.png" width="24" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="1" y="10" width="21" height="22">
    <properties>
     <property name="userData" value="totemHead5"/>
    </properties>
   </object>
   <object id="2" x="-1" y="9" width="24" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="8"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="91">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="circleRadius" type="float" value="8"/>
   <property name="entityTags" propertytype="EntityTags" value="ENEMY"/>
   <property name="gameObject" propertytype="GameObject" value="TOTEM_HEAD_6"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="life" type="int" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/totem_head_6.png" width="30" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="6" y="10" width="23" height="22">
    <properties>
     <property name="userData" value="totemHead6"/>
    </properties>
   </object>
   <object id="2" x="5" y="10" width="24" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="8"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="92">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="ARMOR"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/armor.png" width="24" height="22"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="4" y="8">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="shield"/>
    </properties>
    <polygon points="0,0 5,9 8,10 11,9 16,0 15,-2 1,-2"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="93">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="BOOTS"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/boots.png" width="24" height="22"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10" y="5">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="boots"/>
    </properties>
    <polygon points="0,0 -2,4 -7,7 -5,10 0,13 8,9 9,2 7,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="95">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="HELMET"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/helmet.png" width="24" height="22"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10" y="4">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="helmet"/>
    </properties>
    <polygon points="1,0 -3,3 -4,5 -3,14 8,14 9,5 8,3 4,0"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
