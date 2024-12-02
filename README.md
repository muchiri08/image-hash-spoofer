<h2>Image Hash Spoofer</h2>
To run this project:
<ul>
  <li>
    Clone the project
    <pre>git clone https://github.com/muchiri08/image-hash-spoofer.git</pre>
  </li>
  <li>
    Run <pre>mvn clean package</pre>
    <p>A target folder will be generated with the jar files</p>
  </li>
  <li>
    Run <pre>java -jar target/image-spoofer-1.0-SNAPSHOT-jar-with-dependencies.jar &lt;hexString&gt; &lt;originalImage&gt; &lt;alteredImage&gt;</pre>
    <p>Example</p>
    <pre>java -jar target/image-spoofer-1.0-SNAPSHOT-jar-with-dependencies.jar 0x24 original.jpg altered.jpg</pre>
  </li>
</ul>
<h4>Note:</h4>
The project only deals with jpg.
