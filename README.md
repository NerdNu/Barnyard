Barnyard
========
Manages summonable pets.


Commands
--------

 * `/pet spawn <type>` - Spawn a pet of the specified type. The default
   configured types are: BAT, CHICKEN, COW, HORSE, IRON_GOLEM, MUSHROOM_COW,
   OCELOT, PIG, SHEEP, SQUID and WOLF.
 * `/pet remove [<player>] <id>` - Remove your own pet with the specified
   1-based ID, or the pet of another player. Removing another player's pet
   requires the `barnyard.other.remove` permission.
 * `/pet list [<player>]` - List your own pets or those of another
   player. Listing another player's pets requires the `barnyard.other.list`
   permission.
 * `/pet wear [<id>]` - Wear the specified pet on your head.
 * `/pet ride <id>` - Ride the specified pet.
 * `/pet stack <id> <id> [<id>...]` - Stack your pets one on top of the
   other. The first ID specified is the bottom of the stack.
 * `/pet name <id> [<name>]` - Assign a name to the pet with the specified
   ID, or clear its name if no name is given.
 * `/pet explode <id>` - Remove the specified pet with an explosion.

Configuration
-------------

 * `maximum-pets` - Maximum number of pets per player (default: 3).
 * `allowed-types` - A list of entity type names that can be spawned.
   See [enum EntityType](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html).


Permissions
-----------

 * `barnyard.spawn` - Gives access to the `/pet spawn` command.
 * `barnyard.spawnany` - Gives access to spawn anything.
 * `barnyard.remove` - Gives access to the `/pet remove` command.
 * `barnyard.list` - Gives access to the `/pet list` command.
 * `barnyard.wear` - Gives access to the `/pet wear` command.
 * `barnyard.ride` - Gives access to the `/pet ride` command.
 * `barnyard.stack` - Gives access to the `/pet stack` command.
 * `barnyard.name` - Gives access to the `/pet name` command.
 * `barnyard.explode` - Gives access to the `/pet explode` command.
 * `barnyard.other.remove` - Gives access to the `/pet remove <player> <id> command`.
 * `barnyard.other.list` - Gives access to the `/pet list <player>` command.
 * `barnyard.other.*` - Gives access to all Barnyard moderation commands.
   Equivalent to these permissions:
   * `barnyard.other.remove`
   * `barnyard.other.list`
 * `barnyard.*` - Gives access to all Barnyard commands. Includes all
   permissions listed above.
