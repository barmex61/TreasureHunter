    #ifdef GL_ES
        precision mediump float;
    #endif

    varying vec4 v_color;
    varying vec2 v_texCoords;
    uniform sampler2D u_texture;
    uniform float u_saturation;
    uniform float u_brightness;
    uniform float u_contrast;
    uniform float u_redTint;
    uniform float u_greenTint;
    uniform float u_blueTint;

    void main() {
        vec4 color = texture2D(u_texture, v_texCoords) * v_color;

        // Saturation - dengeli efekt
        float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
        color.rgb = mix(vec3(gray), color.rgb, u_saturation);

        // Contrast - dengeli efekt
        color.rgb = ((color.rgb - 0.5) * u_contrast) + 0.5;

        // Brightness - dengeli efekt
        color.rgb *= u_brightness;

        // Renk tonlaması
        color.r *= u_redTint;
        color.g *= u_greenTint;
        color.b *= u_blueTint;

        // Renk değerlerini 0-1 arasında tut
        color.rgb = clamp(color.rgb, 0.0, 1.0);

        gl_FragColor = color;
    }
