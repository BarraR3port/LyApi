/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, LyMarket
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * https://github.com/Lydark-Studio/LyApi/blob/master/LICENSE
 *
 * Contact: contact@lymarket.net
 */

package net.lymarket.lyapi.spigot.utils.effect;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.color.DustData;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;


/**
 * By default the particle xyz offsets and speed aren't 0, but
 * everything will be 0 by default in this class.
 * Particles are spawned to a location. So all the nearby players can see it.
 * <p>
 * The fields of this class are publicly accessible for ease of use.
 * All the fields can be null except the particle type.
 * <p>
 * For cross-version compatibility, instead of Bukkit's {@link org.bukkit.Color}
 * the java awt {@link Color} class is used.
 * <p>
 * the data field is used to store special particle data, such as colored particles.
 * For colored particles a float list is used since the particle size is a float.
 * The format of float list data for a colored particle is:
 * <code>[r, g, b, size]</code>
 *
 * @author Crypto Morin
 * @version 7.1.0
 * @see XEffect
 */
public class EffectDisplay implements Cloneable {
    
    private static final boolean ISFLAT = XEffect.getParticle( "FOOTSTEP" ) == null;
    private static final Axis[] DEFAULT_ROTATION_ORDER = {Axis.X , Axis.Y , Axis.Z};
    private static final ParticleEffect DEFAULT_EFFECT = ParticleEffect.CLOUD;
    
    public int count;
    public double extra;
    public boolean force;
    public boolean isDust;
    
    private ParticleEffect effect = DEFAULT_EFFECT;
    
    private Location location;
    
    private Callable < Location > locationCaller;
    
    private Vector rotation, offset = new Vector( );
    
    private Axis[] rotationOrder = DEFAULT_ROTATION_ORDER;
    
    private Object data;
    
    private Predicate < Location > onSpawn;
    
    /**
     * Builds a simple EffectDisplay object with cross-version
     * compatible {@link } properties.
     * Only REDSTONE particle type can be colored like this.
     *
     * @param location the location of the display.
     * @param size     the size of the dust.
     *
     * @return a redstone colored dust.
     *
     * @see #simple(Location , ParticleEffect)
     * @since 1.0.0
     */
    
    public static EffectDisplay colored( Location location , int r , int g , int b , float size ){
        return EffectDisplay.simple( location , ParticleEffect.BLOCK_DUST ).withColor( r , g , b , size );
    }
    
    /**
     * Builds a simple EffectDisplay object with cross-version
     * compatible {@link } properties.
     * Only REDSTONE particle type can be colored like this.
     *
     * @param location the location of the display.
     * @param color    the color of the particle.
     * @param size     the size of the dust.
     *
     * @return a redstone colored dust.
     *
     * @see #colored(Location , int , int , int , float)
     * @since 3.0.0
     */
    
    public static EffectDisplay colored( Location location , Color color , float size ){
        return colored( location , color.getRed( ) , color.getGreen( ) , color.getBlue( ) , size );
    }
    
    /**
     * Builds a simple EffectDisplay object.
     * An invocation of this method yields exactly the same result as the expression:
     * <p>
     * <blockquote>
     * new EffectDisplay(particle, location, 1, 0, 0, 0, 0);
     * </blockquote>
     *
     * @param location the location of the display.
     * @param particle the particle of the display.
     *
     * @return a simple EffectDisplay with count 1 and no offset, rotation etc.
     *
     * @since 1.0.0
     */
    
    public static EffectDisplay simple( Location location , ParticleEffect particle ){
        Objects.requireNonNull( particle , "Cannot build EffectDisplay with null particle" );
        EffectDisplay display = new EffectDisplay( );
        display.effect = particle;
        display.location = location;
        return display;
    }
    
    /**
     * @since 6.0.0.1
     */
    
    public static EffectDisplay of( ParticleEffect particle ){
        return simple( null , particle );
    }
    
    /**
     * A quick access method to display a simple particle.
     * An invocation of this method yields the same result as the expression:
     * <p>
     * <blockquote>
     * EffectDisplay.simple(location, particle).spawn();
     * </blockquote>
     *
     * @param location the location of the particle.
     * @param particle the particle to show.
     *
     * @return a simple EffectDisplay with count 1 and no offset, rotation etc.
     *
     * @since 1.0.0
     */
    
    public static EffectDisplay display( Location location , ParticleEffect particle ){
        Objects.requireNonNull( location , "Cannot display particle in null location" );
        EffectDisplay display = simple( location , particle );
        display.spawn( );
        return display;
    }
    
    /**
     * Builds particle settings from a configuration section.
     *
     * @param config the config section for the settings.
     *
     * @return a parsed EffectDisplay from the config.
     *
     * @since 1.0.0
     */
    public static EffectDisplay fromConfig( ConfigurationSection config ){
        return edit( new EffectDisplay( ) , config );
    }
    
    /**
     * Builds particle settings from a configuration section. Keys in config can be :
     * <ul>
     * <li>particle : the particle type.
     * <li>count : the count as integer, at least 0.
     * <li>extra : the particle speed, most of the time.
     * <li>force : true or false, if the particle has force or not.
     * <li>offset : the offset where values are separated by commas "dx, dy, dz".
     * <li>rotation : the rotation of the particles in degrees.
     * <li>color : the data representing color "R, G, B, size" where RGB values are integers
     *             between 0 and 255 and size is a positive (or null) float.
     * <li>blockdata : the data representing block data. Given by a material name that's a block.
     * <li>materialdata : same than blockdata, but with legacy data before 1.12.
     *                    <strong>Do not use this in 1.13 and above.</strong>
     * <li>itemstack : the data representing item. Given by a material name that's an item.
     * </ul>
     *
     * @param display the particle display settings to update.
     * @param config  the config section for the settings.
     *
     * @return the same EffectDisplay, but edited.
     *
     * @since 5.0.0
     */
    
    public static EffectDisplay edit( EffectDisplay display , ConfigurationSection config ){
        Objects.requireNonNull( display , "Cannot edit a null particle display" );
        Objects.requireNonNull( config , "Cannot parse EffectDisplay from a null config section" );
        
        String particleName = config.getString( "particle" );
        ParticleEffect particle = particleName == null ? null : XEffect.getParticle( particleName );
        
        if ( particle != null ) display.effect = particle;
        if ( config.isSet( "count" ) ) display.withCount( config.getInt( "count" ) );
        if ( config.isSet( "extra" ) ) display.withExtra( config.getDouble( "extra" ) );
        if ( config.isSet( "force" ) ) display.forceSpawn( config.getBoolean( "force" ) );
        
        String offset = config.getString( "offset" );
        if ( offset != null ) {
            String[] offsets = StringUtils.split( StringUtils.deleteWhitespace( offset ) , ',' );
            if ( offsets.length >= 3 ) {
                double offsetx = NumberUtils.toDouble( offsets[0] );
                double offsety = NumberUtils.toDouble( offsets[1] );
                double offsetz = NumberUtils.toDouble( offsets[2] );
                display.offset( offsetx , offsety , offsetz );
            } else {
                double masterOffset = NumberUtils.toDouble( offsets[0] );
                display.offset( masterOffset );
            }
        }
        
        String rotation = config.getString( "rotation" );
        if ( rotation != null ) {
            String[] rotations = StringUtils.split( StringUtils.deleteWhitespace( rotation ) , ',' );
            if ( rotations.length >= 3 ) {
                double x = Math.toRadians( NumberUtils.toDouble( rotations[0] ) );
                double y = Math.toRadians( NumberUtils.toDouble( rotations[1] ) );
                double z = Math.toRadians( NumberUtils.toDouble( rotations[2] ) );
                display.rotation = new Vector( x , y , z );
            }
        }
        
        String rotationOrder = config.getString( "rotation-order" );
        if ( rotationOrder != null ) {
            rotationOrder = StringUtils.deleteWhitespace( rotationOrder ).toUpperCase( Locale.ENGLISH );
            display.rotationOrder(
                    Axis.valueOf( String.valueOf( rotationOrder.charAt( 0 ) ) ) ,
                    Axis.valueOf( String.valueOf( rotationOrder.charAt( 1 ) ) ) ,
                    Axis.valueOf( String.valueOf( rotationOrder.charAt( 2 ) ) )
            );
        }
        
        String color = config.getString( "color" ); // array-like "R, G, B"
        String blockdata = config.getString( "blockdata" );       // material name
        String item = config.getString( "itemstack" );            // material name
        String materialdata = config.getString( "materialdata" ); // material name
        
        float size = 1.0f;
        if ( display.data instanceof float[] ) {
            float[] datas = ( float[] ) display.data;
            if ( datas.length >= 4 ) {
                if ( config.isSet( "size" ) ) datas[3] = size = ( float ) config.getDouble( "size" );
                else size = datas[3];
            }
        }
        
        if ( color != null ) {
            String[] colors = StringUtils.split( StringUtils.deleteWhitespace( color ) , ',' );
            if ( colors.length == 1 || colors.length == 3 ) {
                Color parsedColor = Color.white;
                if ( colors.length == 1 ) {
                    try {
                        parsedColor = Color.decode( colors[0] );
                    } catch ( NumberFormatException ex ) {
                        /* I don't think it's worth it.
                        try {
                            parsedColor = (Color) Color.class.getField(colors[0].toUpperCase(Locale.ENGLISH)).get(null);
                        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) { }
                         */
                    }
                } else {
                    parsedColor = new Color( NumberUtils.toInt( colors[0] ) , NumberUtils.toInt( colors[1] ) , NumberUtils.toInt( colors[2] ) );
                }
                
                display.data = new float[]{
                        parsedColor.getRed( ) , parsedColor.getGreen( ) , parsedColor.getBlue( ) ,
                        size
                };
            }
        } else if ( blockdata != null ) {
            Material material = Material.getMaterial( blockdata );
            if ( material != null && material.isBlock( ) ) {
                display.data = material.getData( );
            }
        } else if ( item != null ) {
            Material material = Material.getMaterial( item );
            if ( material != null && display.isItem( material.getId( ) ) ) {
                display.data = new ItemStack( material , 1 );
            }
        } else if ( materialdata != null ) {
            Material material = Material.getMaterial( materialdata );
            if ( material != null && material.isBlock( ) ) {
                display.data = material.getData( );
            }
        }
        
        return display;
    }
    
    /**
     * We don't want to use {@link Location#clone()} since it doesn't copy to constructor and Java's clone method
     * is known to be inefficient and broken.
     *
     * @since 3.0.3
     */
    
    private static Location cloneLocation( Location location ){
        return new Location( location.getWorld( ) , location.getX( ) , location.getY( ) , location.getZ( ) , location.getYaw( ) , location.getPitch( ) );
    }
    
    /**
     * Rotates the given location vector around a certain axis.
     *
     * @param location the location to rotate.
     * @param axis     the axis to rotate the location around.
     * @param rotation the rotation vector that contains the degrees of the rotation. The number is taken from this vector according to the given axis.
     *
     * @since 7.0.0
     */
    public static Vector rotateAround( Vector location , EffectDisplay.Axis axis , Vector rotation ){
        Objects.requireNonNull( axis , "Cannot rotate around null axis" );
        Objects.requireNonNull( rotation , "Rotation vector cannot be null" );
        
        switch ( axis ) {
            case X:
                return rotateAround( location , axis , rotation.getX( ) );
            case Y:
                return rotateAround( location , axis , rotation.getY( ) );
            case Z:
                return rotateAround( location , axis , rotation.getZ( ) );
            default:
                throw new AssertionError( "Unknown rotation axis: " + axis );
        }
    }
    
    /**
     * Rotates the given location vector around a certain axis.
     *
     * @param location the location to rotate.
     *
     * @since 7.0.0
     */
    public static Vector rotateAround( Vector location , double x , double y , double z ){
        rotateAround( location , Axis.X , x );
        rotateAround( location , Axis.Y , y );
        rotateAround( location , Axis.Z , z );
        return location;
    }
    
    /**
     * Rotates the given location vector around a certain axis.
     *
     * @param location the location to rotate.
     * @param axis     the axis to rotate the location around.
     *
     * @since 7.0.0
     */
    public static Vector rotateAround( Vector location , EffectDisplay.Axis axis , double angle ){
        Objects.requireNonNull( location , "Cannot rotate a null location" );
        Objects.requireNonNull( axis , "Cannot rotate around null axis" );
        if ( angle == 0 ) return location;
        
        double cos = Math.cos( angle );
        double sin = Math.sin( angle );
        
        switch ( axis ) {
            case X: {
                double y = location.getY( ) * cos - location.getZ( ) * sin;
                double z = location.getY( ) * sin + location.getZ( ) * cos;
                return location.setY( y ).setZ( z );
            }
            case Y: {
                double x = location.getX( ) * cos + location.getZ( ) * sin;
                double z = location.getX( ) * -sin + location.getZ( ) * cos;
                return location.setX( x ).setZ( z );
            }
            case Z: {
                double x = location.getX( ) * cos - location.getY( ) * sin;
                double y = location.getX( ) * sin + location.getY( ) * cos;
                return location.setX( x ).setY( y );
            }
            default:
                throw new AssertionError( "Unknown rotation axis: " + axis );
        }
    }
    
    public boolean isItem( int id ){
        switch ( id ) {
            case 15:
            case 28:
            case 29:
            case 33:
            case 41:
            case 59:
            case 72:
            case 91:
            case 103:
            case 124:
            case 126:
            case 130:
            case 136:
            case 142:
            case 174:
            case 200:
            case 217:
            case 235:
            case 244:
            case 248:
            case 253:
            case 257:
            case 261:
            case 288:
            case 308:
            case 309:
            case 324:
            case 331:
            case 339:
            case 386:
            case 399:
            case 412:
            case 458:
            case 461:
            case 469:
            case 491:
            case 504:
            case 521:
            case 537:
            case 546:
            case 559:
            case 586:
            case 606:
            case 623:
            case 649:
            case 652:
            case 654:
            case 671:
            case 673:
            case 674:
            case 675:
            case 676:
            case 677:
            case 678:
            case 679:
            case 680:
            case 681:
            case 682:
            case 683:
            case 684:
            case 685:
            case 686:
            case 687:
            case 688:
            case 689:
            case 690:
            case 691:
            case 692:
            case 693:
            case 694:
            case 695:
            case 696:
            case 697:
            case 714:
            case 726:
            case 749:
            case 750:
            case 774:
            case 804:
            case 846:
            case 887:
            case 889:
            case 899:
            case 907:
            case 915:
            case 916:
            case 918:
            case 935:
            case 941:
            case 961:
            case 968:
            case 977:
            case 978:
            case 979:
            case 980:
            case 995:
            case 1003:
            case 1005:
            case 1012:
            case 1020:
            case 1024:
            case 1028:
            case 1031:
            case 1032:
            case 1033:
            case 1037:
            case 1040:
            case 1043:
            case 1044:
            case 1052:
            case 1059:
            case 1061:
            case 1062:
            case 1063:
            case 1073:
            case 1074:
            case 1084:
            case 1086:
            case 1087:
            case 1088:
            case 1093:
            case 1094:
            case 1096:
            case 1101:
            case 1109:
            case 1110:
            case 1111:
            case 1113:
            case 1118:
            case 1119:
            case 1145:
            case 1146:
            case 1147:
            case 1150:
            case 1162:
            case 1163:
            case 1164:
            case 1165:
            case 1166:
            case 1173:
            case 1176:
            case 1178:
            case 1181:
                return false;
            default:
                return true;
        }
    }
    
    /**
     * A simple event that is called after the final calculations of each particle location are applied.
     * You can modify the given location. It's NOT a copy.
     *
     * @param onSpawn a predicate that if returns false, it'll not spawn that particle.
     *
     * @return the same particle display.
     *
     * @since 7.0.0
     */
    public EffectDisplay onSpawn( Predicate < Location > onSpawn ){
        this.onSpawn = onSpawn;
        return this;
    }
    
    /**
     * @since 7.0.0
     */
    public void withParticle( ParticleEffect particle ){
        this.effect = Objects.requireNonNull( particle , "ParticleEffect cannot be null" );
    }
    
    /**
     * Rotates the given xyz with the given rotation radians and
     * adds the to the specified location.
     *
     * @param location the location to add the rotated axis.
     *
     * @return a cloned rotated location.
     *
     * @since 3.0.0
     */
    
    public Location rotate( Location location , double x , double y , double z ){
        if ( location == null )
            throw new IllegalStateException( "Attempting to spawn particle when no location is set" );
        if ( rotation == null ) return cloneLocation( location ).add( x , y , z );
        
        Vector rotate = new Vector( x , y , z );
        rotateAround( rotate , rotationOrder[0] , rotation );
        rotateAround( rotate , rotationOrder[1] , rotation );
        rotateAround( rotate , rotationOrder[2] , rotation );
        
        return cloneLocation( location ).add( rotate );
    }
    
    /**
     * Get the data object. Currently, it can be instance of float[] with [R, G, B, size],
     * or instance of {@link }, {@link MaterialData} for legacy usage or {@link ItemStack}
     *
     * @return the data object.
     *
     * @since 5.1.0
     */
    @SuppressWarnings("deprecation")
    
    public Object getData( ){
        return data;
    }
    
    @Override
    public String toString( ){
        Location location = getLocation( );
        return "EffectDisplay:[" +
                "ParticleEffect=" + effect + ", " +
                "Count=" + count + ", " +
                "Offset:{" + offset.getX( ) + ", " + offset.getY( ) + ", " + offset.getZ( ) + "}, " +
                
                (location != null ? (
                        "Location:{" + location.getWorld( ).getName( ) + location.getX( ) + ", " + location.getY( ) + ", " + location.getZ( ) + "} " +
                                '(' + (locationCaller == null ? "Static" : "Dynamic") + "), "
                ) : "") +
                
                (rotation != null ? (
                        "Rotation:{" + Math.toDegrees( rotation.getX( ) ) + ", " + Math.toRadians( rotation.getY( ) ) + ", " + Math.toDegrees( rotation.getZ( ) ) + "}, "
                ) : "") +
                
                (rotationOrder != DEFAULT_ROTATION_ORDER ? ("RotationOrder:" + Arrays.toString( rotationOrder ) + ", ") : "") +
                
                "Extra=" + extra + ", " +
                "Force=" + force + ", " +
                "Data=" + (data == null ? "null" : data instanceof float[] ? Arrays.toString( ( float[] ) data ) : data);
    }
    
    /**
     * Changes the particle count of the particle settings.
     *
     * @param count the particle count.
     *
     * @return the same particle display.
     *
     * @since 3.0.0
     */
    
    public EffectDisplay withCount( int count ){
        this.count = count;
        return this;
    }
    
    /**
     * In most cases extra is the speed of the particles.
     *
     * @param extra the extra number.
     *
     * @return the same particle display.
     *
     * @since 3.0.1
     */
    
    public EffectDisplay withExtra( double extra ){
        this.extra = extra;
        return this;
    }
    
    /**
     * A displayed particle with force can be seen further
     * away for all player regardless of their particle
     * settings. Force has no effect if specific players
     * are added with {@link #spawn(Location , Player...)}.
     *
     * @param force the force argument.
     *
     * @return the same particle display, but modified.
     *
     * @since 5.0.1
     */
    
    public EffectDisplay forceSpawn( boolean force ){
        this.force = force;
        return this;
    }
    
    /**
     * Adds color properties to the particle settings.
     * The particle must be {@link ParticleEffect#}
     * to get custom colors.
     *
     * @param color the RGB color of the particle.
     * @param size  the size of the particle.
     *
     * @return the same particle display, but modified.
     *
     * @see #colored(Location , Color , float)
     * @since 3.0.0
     */
    
    public EffectDisplay withColor( Color color , float size ){
        return withColor( color.getRed( ) , color.getGreen( ) , color.getBlue( ) , size );
    }
    
    /**
     * @since 7.1.0
     */
    
    public EffectDisplay withColor( float red , float green , float blue , float size ){
        this.data = new float[]{red , green , blue , size};
        return this;
    }
    /*
     *//**
     * Adds data for {@link ParticleEffect#BLOCK_CRACK}, {@link ParticleEffect#BLOCK_DUST}
     * and {@link ParticleEffect#FALLING_DUST} particles. The displayed particle
     * will depend on the given block data for its color.
     * <p>
     * Only works on minecraft version 1.13 and more, because
     * {@link BlockData} didn't exist before.
     *
     * @param blockData the block data that will change the particle data.
     *
     * @return the same particle display, but modified.
     * @since 5.1.0
     *//*
    
    public EffectDisplay withBlock(  BlockData blockData) {
        this.data = blockData;
        return this;
    }*/
    
    /**
     * Adds data for {@link ParticleEffect#}, {@link ParticleEffect#}
     * and {@link ParticleEffect#} particles if the minecraft version is 1.13 or more.
     * <p>
     * If version is at most 1.12, old particles {@link ParticleEffect#},
     * {@link ParticleEffect#} and {@link ParticleEffect#} will support this data.
     *
     * @param materialData the material data that will change the particle data.
     *
     * @return the same particle display, but modified.
     *
     * @see #()
     * @since 5.1.0
     */
    @SuppressWarnings("deprecation")
    
    public EffectDisplay withBlock( MaterialData materialData ){
        this.data = materialData;
        return this;
    }
    
    /**
     * Adds extra data for {@link ParticleEffect#}
     * particle, depending on the given item stack.
     *
     * @param item the item stack that will change the particle data.
     *
     * @return the same particle display, but modified.
     *
     * @since 5.1.0
     */
    
    public EffectDisplay withItem( ItemStack item ){
        this.data = item;
        return this;
    }
    
    
    public Vector getOffset( ){
        return offset;
    }
    
    /**
     * Saves an instance of an entity to track the location from.
     *
     * @param entity the entity to track the location from.
     *
     * @return the same particle settings with the caller added.
     *
     * @since 3.1.0
     */
    
    public EffectDisplay withEntity( Entity entity ){
        return withLocationCaller( entity::getLocation );
    }
    
    /**
     * Sets a caller for location changes.
     *
     * @param locationCaller the caller to call to get the new location.
     *
     * @return the same particle settings with the caller added.
     *
     * @since 3.1.0
     */
    
    public EffectDisplay withLocationCaller( Callable < Location > locationCaller ){
        this.locationCaller = locationCaller;
        return this;
    }
    
    /**
     * Sets the rotation order that the particles should be rotated.
     * Yes,it matters which axis you rotate first as it'll have an impact on the
     * other rotations.
     *
     * @since 7.0.0
     */
    public EffectDisplay rotationOrder( EffectDisplay.Axis first , EffectDisplay.Axis second , EffectDisplay.Axis third ){
        Objects.requireNonNull( first , "First rotation order axis is null" );
        Objects.requireNonNull( second , "Second rotation order axis is null" );
        Objects.requireNonNull( third , "Third rotation order axis is null" );
        
        this.rotationOrder = new Axis[]{first , second , third};
        return this;
    }
    
    /**
     * Gets the location of an entity if specified or the constant location.
     *
     * @return the location of the particle.
     *
     * @since 3.1.0
     */
    
    public Location getLocation( ){
        try {
            return locationCaller == null ? location : locationCaller.call( );
        } catch ( Exception e ) {
            e.printStackTrace( );
            return location;
        }
    }
    
    /**
     * Sets the location that this particle should spawn.
     *
     * @param location the new location.
     *
     * @since 7.0.0
     */
    public EffectDisplay withLocation( Location location ){
        this.location = location;
        return this;
    }
    
    /**
     * Adjusts the rotation settings to face the entity's direction.
     * Only some of the shapes support this method.
     *
     * @param entity the entity to face.
     *
     * @return the same particle display.
     *
     * @see #rotate(Vector)
     * @since 3.0.0
     */
    
    public EffectDisplay face( Entity entity ){
        return face( Objects.requireNonNull( entity , "Cannot face null entity" ).getLocation( ) );
    }
    
    /**
     * Adjusts the rotation settings to face the locations pitch and yaw.
     * Only some of the shapes support this method.
     *
     * @param location the location to face.
     *
     * @return the same particle display.
     *
     * @see #rotate(Vector)
     * @since 6.1.0
     */
    
    public EffectDisplay face( Location location ){
        Objects.requireNonNull( location , "Cannot face null location" );
        // We add 90 degrees to compensate for the non-standard use of pitch degrees in Minecraft.
        this.rotation = new Vector( Math.toRadians( location.getPitch( ) + 90 ) , Math.toRadians( -location.getYaw( ) ) , 0 );
        return this;
    }
    
    /**
     * Clones the location of this particle display and adds xyz.
     *
     * @param x the x to add to the location.
     * @param y the y to add to the location.
     * @param z the z to add to the location.
     *
     * @return the cloned location.
     *
     * @see #clone()
     * @since 1.0.0
     */
    
    public Location cloneLocation( double x , double y , double z ){
        return location == null ? null : cloneLocation( location ).add( x , y , z );
    }
    
    /**
     * Clones this particle settings and adds xyz to its location.
     *
     * @param x the x to add.
     * @param y the y to add.
     * @param z the z to add.
     *
     * @return the cloned EffectDisplay.
     *
     * @see #clone()
     * @since 1.0.0
     */
    
    public EffectDisplay cloneWithLocation( double x , double y , double z ){
        EffectDisplay display = clone( );
        if ( location == null ) return display;
        display.location.add( x , y , z );
        return display;
    }
    
    /**
     * Clones this particle settings.
     *
     * @return the cloned EffectDisplay.
     *
     * @see #cloneWithLocation(double , double , double)
     * @see #cloneLocation(double , double , double)
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    
    public EffectDisplay clone( ){
        EffectDisplay display = EffectDisplay.of( effect )
                .withLocationCaller( locationCaller )
                .withCount( count ).offset( offset.clone( ) )
                .forceSpawn( force ).onSpawn( onSpawn );
        
        if ( location != null ) display.location = cloneLocation( location );
        if ( rotation != null ) display.rotation = this.rotation.clone( );
        display.rotationOrder = this.rotationOrder;
        display.data = data;
        return display;
    }
    
    /**
     * Rotates the particle position based on this vector.
     *
     * @param vector the vector to rotate from. The xyz values of this vector must be radians.
     *
     * @see #rotate(double , double , double)
     * @since 1.0.0
     */
    
    public EffectDisplay rotate( Vector vector ){
        Objects.requireNonNull( vector , "Cannot rotate EffectDisplay with null vector" );
        if ( rotation == null ) rotation = vector;
        else rotation.add( vector );
        return this;
    }
    
    /**
     * Rotates the particle position based on the xyz radians.
     * Rotations are only supported for some shapes in {@link XEffect}.
     * Rotating some of them can result in weird shapes.
     *
     * @see #rotate(Vector)
     * @since 3.0.0
     */
    
    public EffectDisplay rotate( double x , double y , double z ){
        return rotate( new Vector( x , y , z ) );
    }
    
    /**
     * Set the xyz offset of the particle settings.
     *
     * @since 1.1.0
     */
    
    public EffectDisplay offset( double x , double y , double z ){
        return offset( new Vector( x , y , z ) );
    }
    
    /**
     * Set the xyz offset of the particle settings.
     *
     * @since 7.0.0
     */
    
    public EffectDisplay offset( Vector offset ){
        this.offset = Objects.requireNonNull( offset , "ParticleEffect offset cannot be null" );
        return this;
    }
    
    /**
     * Gets the rotation vector of this particle once spawned.
     *
     * @return a rotation that will be applied.
     *
     * @since 6.1.0
     */
    
    public Vector getRotation( ){
        return rotation;
    }
    
    /**
     * Sets a new rotation vector ignoring previous ones.
     *
     * @param rotation the new rotation.
     *
     * @since 7.0.0
     */
    public void setRotation( Vector rotation ){
        this.rotation = rotation;
    }
    
    /**
     * Set the xyz offset of the particle settings to a single number.
     *
     * @since 6.0.0.1
     */
    
    public EffectDisplay offset( double offset ){
        return offset( offset , offset , offset );
    }
    
    /**
     * When a particle is set to be directional it'll only
     * spawn one particle and the xyz offset values are used for
     * the direction of the particle.
     * <p>
     * Colored particles in 1.12 and below don't support this.
     *
     * @return the same particle display.
     *
     * @see #isDirectional()
     * @since 1.1.0
     */
    
    public EffectDisplay directional( ){
        count = 0;
        return this;
    }
    
    /**
     * Check if this particle setting is a directional particle.
     *
     * @return true if the particle is directional, otherwise false.
     *
     * @see #directional()
     * @since 2.1.0
     */
    public boolean isDirectional( ){
        return count == 0;
    }
    
    /**
     * Spawns the particle at the current location.
     *
     * @since 2.0.1
     */
    public void spawn( ){
        spawn( getLocation( ) );
    }
    
    /**
     * Adds xyz of the given vector to the cloned location before
     * spawning particles.
     *
     * @param location the xyz to add.
     *
     * @since 1.0.0
     */
    
    public Location spawn( Vector location ){
        Objects.requireNonNull( location , "Cannot add xyz of null vector to EffectDisplay" );
        return spawn( location.getX( ) , location.getY( ) , location.getZ( ) );
    }
    
    /**
     * Adds xyz to the cloned location before spawning particle.
     *
     * @since 1.0.0
     */
    
    public Location spawn( double x , double y , double z ){
        return spawn( rotate( getLocation( ) , x , y , z ) );
    }
    
    /**
     * Displays the particle in the specified location.
     * This method does not support rotations if used directly.
     *
     * @param loc the location to display the particle at.
     *
     * @see #spawn(double , double , double)
     * @since 2.1.0
     */
    
    public Location spawn( Location loc ){
        return spawn( loc , ( Player[] ) null );
    }
    
    /**
     * Displays the particle in the specified location.
     * This method does not support rotations if used directly.
     *
     * @param loc     the location to display the particle at.
     * @param players if this particle should only be sent to specific players. Shouldn't be empty.
     *
     * @see #spawn(double , double , double)
     * @since 5.0.0
     */
    
    public Location spawn( Location loc , Player... players ){
        if ( loc == null ) throw new IllegalStateException( "Attempting to spawn particle when no location is set" );
        if ( onSpawn != null ) {
            if ( !onSpawn.test( loc ) ) return loc;
        }
        double offSetX = offset.getX( );
        double offSetY = offset.getY( );
        double offsetz = offset.getZ( );
        
        if ( data != null && data instanceof float[] ) {
            float[] datas = ( float[] ) data;
            
            if ( ISFLAT && effect.equals( ParticleEffect.BLOCK_DUST ) ) {
                DustData dust = new DustData( ( int ) datas[0] , ( int ) datas[1] , ( int ) datas[2] , datas[3] );
                if ( players == null ) {
                    new ParticleBuilder( effect , loc )
                            .setAmount( count )
                            .setOffsetX( ( float ) offSetX )
                            .setOffsetY( ( float ) offSetY )
                            .setOffsetZ( ( float ) offsetz )
                            .setSpeed( ( float ) extra )
                            .setParticleData( dust )
                            .display( );
                } else {
                    new ParticleBuilder( effect , loc )
                            .setAmount( count )
                            .setOffsetX( ( float ) offSetX )
                            .setOffsetY( ( float ) offSetY )
                            .setOffsetZ( ( float ) offsetz )
                            .setSpeed( ( float ) extra )
                            .setParticleData( dust )
                            .display( players );
                }
                
                //else for (Player player : players) player.spigot().playEffect( effect , loc, count, offsetx, offsety, offsetz, extra, dust);
                
            } else if ( isDirectional( ) ) {
                // With count=0, color on offset e.g. for MOB_SPELL or 1.12 REDSTONE
                float[] rgb = {datas[0] / 255f , datas[1] / 255f , datas[2] / 255f};
                if ( players == null ) {
                    new ParticleBuilder( effect , loc )
                            .setAmount( count )
                            .setOffsetX( rgb[0] )
                            .setOffsetY( rgb[1] )
                            .setOffsetZ( rgb[2] )
                            .display( );
                } else {
                    new ParticleBuilder( effect , loc )
                            .setAmount( count )
                            .setOffsetX( rgb[0] )
                            .setOffsetY( rgb[1] )
                            .setOffsetZ( rgb[2] )
                            .display( players );
                }
                
            } else {
                // Else color can't have any effect, keep default param
                if ( players == null ) {
                    new ParticleBuilder( effect , loc )
                            .setAmount( count )
                            .setOffsetX( ( float ) offSetX )
                            .setOffsetY( ( float ) offSetY )
                            .setOffsetZ( ( float ) offsetz )
                            .setSpeed( ( float ) extra )
                            .display( );
                } else {
                    new ParticleBuilder( effect , loc )
                            .setAmount( count )
                            .setOffsetX( ( float ) offSetX )
                            .setOffsetY( ( float ) offSetY )
                            .setOffsetZ( ( float ) offsetz )
                            .setSpeed( ( float ) extra )
                            .display( players );
                }
            }
        } else {
            // Checks without data or block crack, block dust, falling dust, item crack or if data isn't right type
            
            if ( players == null ) {
                new ParticleBuilder( effect , loc )
                        .setAmount( count )
                        .setOffsetX( ( float ) offSetX )
                        .setOffsetY( ( float ) offSetY )
                        .setOffsetZ( ( float ) offsetz )
                        .setSpeed( ( float ) extra )
                        .display( );
            } else
                new ParticleBuilder( effect , loc )
                        .setAmount( count )
                        .setOffsetX( ( float ) offSetX )
                        .setOffsetY( ( float ) offSetY )
                        .setOffsetZ( ( float ) offsetz )
                        .setSpeed( ( float ) extra )
                        .display( players );
        }
        
        return loc;
    }
    
    /**
     * As an alternative to {@link } because it doesn't exist in 1.12
     *
     * @since 7.0.0
     */
    public enum Axis {X, Y, Z}
}