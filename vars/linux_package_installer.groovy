#!/usr/bin/env groovy
import org.apache.commons.lang.StringUtils

def install(String package) {
  def unzipInstalled = sh(script: 'which $package', returnStatus: true)
                    def packageManager = ""
 
                    println("unzipInstalled: ${unzipInstalled}")             

                    def distroType = sh(script: 'cat /etc/os-release', returnStdout: true)
                    
                    if(distroType.contains('ubuntu') || distroType.contains('debian')){
                        packageManager = 'apt-get'
                    } else if (distroType.contains('rhel') || distroType.contains('centos')) {
                           packageManager = 'yum'
                    } else {
                        echo "Unsupported Linux distribution"
                        return
                    }

                    if (unzipInstalled != 0) {
                           echo "unzip is not installed. Installing..."
                            sh "sudo ${packageManager} update"
                            sh "sudo ${packageManager} install -y unzip"
                            echo "unzip installed successfully."   
                    } else {
                        echo "unzip is already installed."
                    }       
}
