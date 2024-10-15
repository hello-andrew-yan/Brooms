<p align="center">
  <img src="assets/icon.png" alt="Brooms Icon">
</p>
<h1 align="center">
  Brooms
</h1>
<br>
<p align="center">
  <a href="https://github.com/hello-andrew-yan/spigot-nms-brooms/commits/master">
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/hello-andrew-yan/spigot-nms-brooms?style=flat-square""></a>
  <a href="https://github.com/hello-andrew-yan/spigot-nms-brooms/issues">
    <img alt="GitHub issues" src="https://img.shields.io/github/issues-raw/hello-andrew-yan/spigot-nms-brooms?style=flat-square""></a>
  <a href="https://github.com/hello-andrew-yan/spigot-nms-brooms/pulls">
    <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr-raw/hello-andrew-yan/spigot-nms-brooms?style=flat-square""></a>
</p>
<p align="center">
 This is an independant and refactored implementation of a brooms plugin I worked on in a previous server. It primarily uses NMS to create a custom flying broom entity that can be controlled by the player.
</p>

---

## Implementation
Currently the plugin mounts you to a `BroomEntity` on `LEFT CLICK` and dies on `DISMOUNT`. Once mounted, you can use the standard movement controls when riding a `Horse` to fly in the air.

- The speed of the broom utilises the lerp function to interpolate between different speed values.
- Directional controls are clamped to prevent diagonal movement from being faster than both forward and sideways movement.
- The velocity of the broom carries over to the rider once dismounted.

### Entity
We will be using the ``Zombie`` class to represent the broom, moreso any entity that can equip an item in the helmet slot. This allows us to incorporate custom models to the flying broom entity.

The **adult** zombie hitbox is the same as the player meaning it's an accurate representation of where the player can and cannot go. This becomes especially important when we discuss **dismounting**. While we may try to manually modify the hitbox ourselves, I'd rather avoid the client and server communicating with conflicting information.

### Movement

We leverage the ability to reference the player's input controls without the usage of external packet libraries such as `ProtocolLib` by referencing the `xxa` and `zza` values in the `Player` class of NMS. Afterwards we can retrieve the vector for forward movement using the direction of the player (not the head direction) and the vector for sideways movement, the cross product of the forward movement with the down vector.

<p align="center"><img src="assets/diagram.png" alt="Cross Product Diagram"></p>

```java

double xSpeedMultiplier = 1.2;
double zSpeedMultiplier = 0.7;

...

x = lerp(x, player.xxa != 0 ? player.xxa : 0, LERP_FACTOR);
z = lerp(z, player.zza != 0 ? player.zza : 0, LERP_FACTOR);

Vector forward = player.getBukkitEntity().getLocation().getDirection();
Vector sideways = forward.clone().crossProduct(new Vector(0, -1, 0));
Vector move = forward.multiply(z * zSpeedMultiplier).add(sideways.multiply(x * xSpeedMultiplier));
```


## To-Do List
- Refactor and implement the ability to boost the brooms speed. With the health bar of the entity serving as the broom meter.
- Refactor and implement the ability to incorporate configurable brooms with differing stats.
- Implement a method to force dismount / remove all broom entites when the server closes.

## Contributing
Feel free to contribute to this project by submitting bug reports, feature requests, or pull requests on the [GitHub repository](https://github.com/hello-andrew-yan/spigot-nms-brooms).

## License
This project is licensed under the [MIT License](LICENSE).

---

<p align="right">
  <a target="_blank" href="https://icons8.com/icon/8068/broom">Broom</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
</p>
