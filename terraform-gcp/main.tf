terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
    }
  }
}

provider "google" {
  credentials = file("burner-syebokha-86fde497ce4a.json")

  project = "burner-syebokha"
  region  = "us-central1"
  zone    = "us-central1-a"
}

resource "google_compute_instance" "default" {
  name         = "wishlist-vm"
  machine_type = "e2-medium"
  zone         = "us-central1-a"
  tags         = ["ssh"]

  metadata = {
    enable-oslogin = "TRUE"
  }
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  # Install Flask
  metadata_startup_script = "sudo apt-get update; sudo apt-get install git openjdk-17-jdk wget -y; git clone https://github.com/Ali-Bokhari/Wishlists.git; wget -qO - https://www.mongodb.org/static/pgp/server-5.0.asc | sudo apt-key add -; echo 'deb http://repo.mongodb.org/apt/debian buster/mongodb-org/5.0 main' | sudo tee /etc/apt/sources.list.d/mongodb-org-5.0.list; sudo apt-get update; sudo apt-get install -y mongodb-org; sudo systemctl start mongod; java -jar Wishlists/target/wishlists-0.0.1-SNAPSHOT.jar"

  network_interface {
    network = "default"

    access_config {
      # Include this section to give the VM an external IP address
    }
  }
}

resource "google_compute_firewall" "ssh" {
  name = "allow-ssh"
  allow {
    ports    = ["22"]
    protocol = "tcp"
  }
  direction     = "INGRESS"
  network       = "default"
  priority      = 1000
  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["ssh"]
}

resource "google_compute_firewall" "flask" {
  name    = "flask-app-firewall"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }
  source_ranges = ["0.0.0.0/0"]
}

output "Web-server-URL" {
  value = join("",["http://",google_compute_instance.default.network_interface.0.access_config.0.nat_ip,":8080"])
}
